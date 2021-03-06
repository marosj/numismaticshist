package com.mjurik.web.services;

import com.mjurik.web.crawler.db.CoinPersistence;
import com.mjurik.web.crawler.db.EuronEuPersistence;
import com.mjurik.web.crawler.db.IgnoredPathPersistence;
import com.mjurik.web.crawler.db.NumEuPersistence;
import com.mjurik.web.crawler.db.entity.Coin;
import com.mjurik.web.crawler.db.entity.EuronEuResult;
import com.mjurik.web.crawler.db.entity.NumEuResult;
import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.data.CrawlerSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Marian Jurik on 19.9.2015.
 */
public class ResultsService {

    private static ResultsService instance;

    public static ResultsService getInstance() {
        if (instance == null) {
            instance = new ResultsService();
        }
        return instance;
    }

    public List<CrawlerResult> findAll(String stringFilter) {
        List<CrawlerResult> result = new ArrayList<>();

        List<NumEuResult> numEuResults = NumEuPersistence.INSTANCE.listUnprocessed();
        if (numEuResults != null) {
            for (NumEuResult numEuResult : numEuResults) {
                CrawlerResult cr = toCrawlerResult(numEuResult);
                addToResultIfMatches(stringFilter, result, cr);
            }
        }

        List<EuronEuResult> euronEuResults = EuronEuPersistence.INSTANCE.listUnprocessed();
        if (euronEuResults != null) {
            for (EuronEuResult euronEuResult : euronEuResults) {
                CrawlerResult cr = toCrawlerResult(euronEuResult);
                addToResultIfMatches(stringFilter, result, cr);
            }
        }
        return result;
    }

    public List<CrawlerResult> findUnprocessedWithExactMatch() {
        List<CrawlerResult> result = new ArrayList<>();

        List<NumEuResult> numEuResults = NumEuPersistence.INSTANCE.listUnprocessedWithProcessedMatch();
        if (numEuResults != null) {
            result.addAll(numEuResults.stream().map(this::toCrawlerResult).collect(Collectors.toList()));
        }

        List<EuronEuResult> euronEuResults = EuronEuPersistence.INSTANCE.listUnprocessedWithProcessedMatch();
        if (euronEuResults != null) {
            result.addAll(euronEuResults.stream().map(this::toCrawlerResult).collect(Collectors.toList()));
        }
        return result;
    }

    private void addToResultIfMatches(String stringFilter, List<CrawlerResult> result, CrawlerResult cr) {
        boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                || cr.matchFulltextSearch(stringFilter);
        if (passesFilter) {
            try {
                result.add(cr.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    public void ignore(CrawlerSource source, String id, String path) {
        IgnoredPathPersistence.INSTANCE.persistIgnoredPath(source.toString(), path);
        setAsProcessed(source, id);
    }

    public void saveAsNewCoin(Coin coin, CrawlerResult crawlerResult) {
        CoinPersistence.INSTANCE.persistNewCoin(coin);
        setAsProcessed(crawlerResult.getSource(), crawlerResult.getId());
    }

    private void setAsProcessed(CrawlerSource source, String id) {
        switch (source) {
            case NUMIZMATIK:
                NumEuPersistence.INSTANCE.setAsProcessed(id);
                break;
            case EURONUMIS:
                EuronEuPersistence.INSTANCE.setAsProcessed(id);
                break;
        }
    }

    private CrawlerResult toCrawlerResult(EuronEuResult euronEuResult) {
        CrawlerResult cr = new CrawlerResult();
        cr.setSource(CrawlerSource.EURONUMIS);
        cr.setId(euronEuResult.getId());
        cr.setEan(euronEuResult.getEan());
        cr.setName(euronEuResult.getName());
        cr.setPath(euronEuResult.getPath());
        cr.setPrice(euronEuResult.getPrice());
        cr.setVariant(euronEuResult.getVariant());
        cr.setProcessed(euronEuResult.getStartDate());
        return cr;
    }

    private CrawlerResult toCrawlerResult(NumEuResult numEuResult) {
        CrawlerResult cr = new CrawlerResult();
        cr.setSource(CrawlerSource.NUMIZMATIK);
        cr.setId(numEuResult.getId());
        cr.setEan(numEuResult.getEan());
        cr.setName(numEuResult.getName());
        cr.setPath(numEuResult.getPath());
        cr.setPrice(numEuResult.getPrice());
        cr.setVariant(numEuResult.getVariant());
        cr.setProcessed(numEuResult.getStartDate());
        return cr;
    }
}
