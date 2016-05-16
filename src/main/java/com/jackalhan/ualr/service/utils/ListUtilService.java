package com.jackalhan.ualr.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jackalhan on 4/21/16.
 */
@Component
public class ListUtilService {

    private final Logger log = LoggerFactory.getLogger(ListUtilService.class);

    private static ListUtilService singleton;

    private ListUtilService(){ }

    public static synchronized ListUtilService getInstance( ) {
        if (singleton == null)
            singleton=new ListUtilService();
        return singleton;
    }

    public <T> Predicate<T> distinctByKey(Function<? super T,Object> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
