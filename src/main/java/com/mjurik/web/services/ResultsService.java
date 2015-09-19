package com.mjurik.web.services;

import java.util.ArrayList;
import java.util.List;

import com.mjurik.web.forms.CrawlerResult;

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
        List<CrawlerResult> available = new ArrayList<>();
        CrawlerResult cr1 = new CrawlerResult();
        cr1.setId("1");
        cr1.setFirstName("First");
        cr1.setLastName("Last");
        available.add(cr1);
        CrawlerResult cr2 = new CrawlerResult();
        cr2.setId("2");
        cr2.setFirstName("Second");
        cr2.setLastName("Second Last");
        available.add(cr2);

        List<CrawlerResult> result = new ArrayList<>();
        for (CrawlerResult crawlerResult : available) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || crawlerResult.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    result.add(crawlerResult.clone());
                }
            } catch (CloneNotSupportedException e) {

            }
        }
        return result;
    }
}
