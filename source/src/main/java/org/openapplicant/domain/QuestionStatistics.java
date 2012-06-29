package org.openapplicant.domain;

import java.math.BigDecimal;

import javax.persistence.Transient;

/**
 * 
 * QuestionStatistics is a simple bean for managing question-related statistics
 * @see QuestionDAO.getQuestionStatisticsForId()
 * 
 * @author mrw
 *
 */
// TODO rename me to Statistics
public class QuestionStatistics {

	
	private BigDecimal activeMean     = BigDecimal.ZERO;
	private BigDecimal benchmarkMean  = BigDecimal.ZERO;
	private BigDecimal futureMean     = BigDecimal.ZERO;
	private BigDecimal hiredMean      = BigDecimal.ZERO;
	private BigDecimal rejectedMean   = BigDecimal.ZERO;
	
    private BigDecimal min			  = BigDecimal.ZERO;
	private BigDecimal mean           = BigDecimal.ZERO;
    private BigDecimal max 			  = BigDecimal.ZERO;
    
	private BigDecimal stddev         = BigDecimal.ZERO;
	
	public QuestionStatistics() {}
	
	public QuestionStatistics(	Object min,
								Object mean,
								Object max,
								Object stddev) {
		this.min = (BigDecimal) min;
		this.mean = (BigDecimal) mean;
		this.max = (BigDecimal) max;
		this.stddev = (BigDecimal) (stddev == null ? BigDecimal.ZERO : stddev);
	}
	
	public BigDecimal getMax() {
		return max;
	}
	public void setMax(BigDecimal max) {
		this.max = max;
	}
	public BigDecimal getMin() {
		return min;
	}
	public void setMin(BigDecimal min) {
		this.min = min;
	}
	public BigDecimal getBenchmarkMean() {
		return benchmarkMean;
	}
	public void setBenchmarkMean(BigDecimal benchmarkMean) {
		this.benchmarkMean = benchmarkMean;
	}
	public BigDecimal getHiredMean() {
		return hiredMean;
	}
	public void setHiredMean(BigDecimal hiredMean) {
		this.hiredMean = hiredMean;
	}
	public BigDecimal getRejectedMean() {
		return rejectedMean;
	}
	public void setRejectedMean(BigDecimal rejectedMean) {
		this.rejectedMean = rejectedMean;
	}
	public BigDecimal getStddev() {
		return stddev;
	}
	public void setStddev(BigDecimal stddev) {
		this.stddev = stddev;
	}
	public BigDecimal getMean() {
		return mean;
	}
	public void setMean(BigDecimal mean) {
		this.mean = mean;
	}
	public void setActiveMean(BigDecimal activeMean) {
		this.activeMean = activeMean;
	}
	public BigDecimal getActiveMean() {
		return activeMean;
	}
	public BigDecimal getFutureMean() {
		return futureMean;
	}
	public void setFutureMean(BigDecimal futureMean) {
		this.futureMean = futureMean;
	}
	
}
