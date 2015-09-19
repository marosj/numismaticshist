package com.mjurik.web.services;

import java.util.ArrayList;
import java.util.List;

import com.mjurik.web.crawler.db.EuronEuPersistence;
import com.mjurik.web.crawler.db.IgnoredPathPersistence;
import com.mjurik.web.crawler.db.NumEuPersistence;
import com.mjurik.web.crawler.db.entity.EuronEuResult;
import com.mjurik.web.crawler.db.entity.NumEuResult;
import com.mjurik.web.data.CrawlerResult;
import com.mjurik.web.data.CrawlerSource;

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
            try {
                for (NumEuResult numEuResult : numEuResults) {
                    CrawlerResult cr = toCrawlerResult(numEuResult);
                    boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                            || cr.matchFulltextSearch(stringFilter);
                    if (passesFilter) {
                        result.add(cr.clone());
                    }
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        List<EuronEuResult> euronEuResults = EuronEuPersistence.INSTANCE.listUnprocessed();
        if (euronEuResults != null) {
            try {
                for (EuronEuResult euronEuResult : euronEuResults) {
                    CrawlerResult cr = toCrawlerResult(euronEuResult);
                    boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                            || cr.matchFulltextSearch(stringFilter);
                    if (passesFilter) {
                        result.add(cr.clone());
                    }
                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void ignore(CrawlerSource source, String id, String path) {
        IgnoredPathPersistence.INSTANCE.persistIgnoredPath(source.toString(), path);
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
        return cr;
    }
}
