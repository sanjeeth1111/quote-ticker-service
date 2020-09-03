package com.quote.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.quote.dto.Quote;

public class QuoteEvent implements Delayed {
	private Quote quote;
	
	public QuoteEvent(Quote quote) {
		this.quote = quote;
	}
	
	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	@Override
	public int compareTo(Delayed that) {
		long result = this.getDelay(TimeUnit.NANOSECONDS) - that.getDelay(TimeUnit.NANOSECONDS);
		if (result < 0) {
			return -1;
		} else if (result > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		 LocalDateTime now = LocalDateTime.now();
		 long diff = now.until(quote.getTimestamp().plus(10,ChronoUnit.MINUTES), ChronoUnit.MILLIS);
		 return unit.convert(diff, TimeUnit.MILLISECONDS);
	}

	@Override
	public String toString() {
		return "QuoteEvent [symbol=" + quote.getSymbol() + ", timestamp=" + quote.getTimestamp() + "]";
	}
}
