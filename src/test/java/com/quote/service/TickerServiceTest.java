package com.quote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.quote.dto.Quote;
import com.quote.service.QuoteService;

public class TickerServiceTest {
	private static final Logger log = LoggerFactory.getLogger(TickerServiceTest.class);
	private static QuoteService service;
	private static ScheduledExecutorService executor;
	

	@BeforeAll
	static void setupData() {
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(new QuoteService.QuoteCacheCleaner(), 1, 1, TimeUnit.SECONDS);
		service = new QuoteService(executor);
	}
	
	@Test
	void testInit() {
		assertNotNull(service);
	}

	
	

	@Test
	public void testSaveQuote() {		
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusSeconds(2));		
		service.saveQuote(q);

		assertEquals(1, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast20Min() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(20));
	
		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(0, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast10Min1sec() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(10).minusSeconds(1));

		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(0, service.getTopTickers().size());
		service.cleanUp();
	}
	
	@Test
	public void testSaveQuote_TimePast9Min58Sec() throws Exception{
		Quote q = new Quote();
		q.setSymbol("A");
		q.setChangeDirection("up");
		q.setPriceTraded(3300);
		q.setChangeInAmount(23.04);
		q.setSharesTraded("22k");
		q.setTimestamp(LocalDateTime.now().minusMinutes(10).plusSeconds(2));

		service.saveQuote(q);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			log.error(e.getMessage());
		}
		assertEquals(1, service.getTopTickers().size());
		service.cleanUp();
	}
	
}
