package aic12.project3.service.statistics;

import aic12.project3.common.beans.StatisticsBean;

public interface Statistics
{
    StatisticsBean getStatistics();

    StatisticsBean recalculateStatistics();
}
