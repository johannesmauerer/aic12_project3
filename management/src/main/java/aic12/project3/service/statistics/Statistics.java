package aic12.project3.service.statistics;

import aic12.project3.common.beans.StatisticsBean;

public interface Statistics
{
    /**
     * Returns the statistics bean with those values calculated in advance. If they were not calculated yet they are now.
     */
    StatisticsBean getStatistics();

    /**
     * Calculates the statistics and returns them. 
     */
    StatisticsBean calculateStatistics();
}