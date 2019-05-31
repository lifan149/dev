package com.zhaohg.lucene.demo2;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IndexDao {
    private static final Logger logger = LoggerFactory.getLogger(IndexDao.class);

    public void save(Article article) {
        Document doc = DocumentUtils.article2Document(article);
        IndexWriter indexWriter = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter = new IndexWriter(LuceneUtils.getDirectory(), config);
            indexWriter.addDocument(doc);
        } catch (Exception e) {
            logger.error("IndexDao.save error", e);
        } finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

    public void delete(String id) {
        IndexWriter indexWriter = null;
        try {
            Term term = new Term("id", id);
            IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter = new IndexWriter(LuceneUtils.getDirectory(), config);
            indexWriter.deleteDocuments(term);// 删除含有指定term的所有文档
        } catch (Exception e) {
            logger.error("IndexDao.save error", e);
        } finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

    public void update(Article article) {
        Document doc = DocumentUtils.article2Document(article);
        IndexWriter indexWriter = null;
        try {
            Term term = new Term("id", article.getId().toString());
            IndexWriterConfig config = new IndexWriterConfig(LuceneUtils.getAnalyzer());
            indexWriter = new IndexWriter(LuceneUtils.getDirectory(), config);
            indexWriter.updateDocument(term, doc);// 先删除，后创建。
        } catch (Exception e) {
            logger.error("IndexDao.save error", e);
        } finally {
            LuceneUtils.closeIndexWriter(indexWriter);
        }
    }

    public QueryResult search(String queryString, int firstResult, int maxResult) {
        List<Article> list = new ArrayList<>();
        try {
            DirectoryReader ireader = DirectoryReader.open(LuceneUtils.getDirectory());
            // 2、第二步，创建搜索器
            IndexSearcher isearcher = new IndexSearcher(ireader);

            // 3、第三步，类似SQL，进行关键字查询
            String[] fields = {"title", "content"};
            QueryParser parser = new MultiFieldQueryParser(fields, LuceneUtils.getAnalyzer());
            Query query = parser.parse(queryString);

            TopDocs topDocs = isearcher.search(query, firstResult + maxResult);
            int count = topDocs.totalHits;// 总记录数
            System.out.println("总记录数为：" + topDocs.totalHits);// 总记录数
            ScoreDoc[] hits = topDocs.scoreDocs;// 第二个参数，指定最多返回前n条结果

            // 高亮
            Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
            Scorer source = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter, source);

            // 摘要
//          Fragmenter fragmenter = new SimpleFragmenter(5);
//          highlighter.setTextFragmenter(fragmenter);

            // 处理结果
            int endIndex = Math.min(firstResult + maxResult, hits.length);
            for (int i = firstResult; i < endIndex; i++) {
                Document hitDoc = isearcher.doc(hits[i].doc);
                Article article = DocumentUtils.document2Ariticle(hitDoc);
                //
                String text = highlighter.getBestFragment(LuceneUtils.getAnalyzer(), "content", hitDoc.get("content"));
                if (text != null) {
                    article.setContent(text);
                }
                list.add(article);
            }
            ireader.close();
            return new QueryResult(count, list);
        } catch (Exception e) {
            logger.error("IndexDao.search error", e);
        }
        return null;
    }
}