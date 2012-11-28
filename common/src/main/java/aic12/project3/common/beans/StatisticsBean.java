package aic12.project3.common.beans;

public class StatisticsBean
{
    private long averageProcessingDurationPerTweet;
    private long averageTotalDurationPerTweet;
    private long averageDurationPerRequest;
    private long maximumDurationOfRequest;
    private long minimumDurationOfRequest;

    public StatisticsBean()
    {
        averageProcessingDurationPerTweet = -1;
        averageTotalDurationPerTweet = -1;
        averageDurationPerRequest = -1;
        maximumDurationOfRequest = -1;
        minimumDurationOfRequest = -1;
    }

    public long getAverageDurationPerRequest()
    {
        return averageDurationPerRequest;
    }

    public void setAverageDurationPerRequest(long averageDurationPerRequest)
    {
        this.averageDurationPerRequest = averageDurationPerRequest;
    }

    public long getMaximumDurationOfRequest()
    {
        return maximumDurationOfRequest;
    }

    public void setMaximumDurationOfRequest(long maximumDurationOfRequest)
    {
        this.maximumDurationOfRequest = maximumDurationOfRequest;
    }

    public long getMinimumDurationOfRequest()
    {
        return minimumDurationOfRequest;
    }

    public void setMinimumDurationOfRequest(long minimumDurationOfRequest)
    {
        this.minimumDurationOfRequest = minimumDurationOfRequest;
    }

    public long getAverageProcessingDurationPerTweet()
    {
        return averageProcessingDurationPerTweet;
    }

    public void setAverageProcessingDurationPerTweet(long averageProcessingDurationPerTweet)
    {
        this.averageProcessingDurationPerTweet = averageProcessingDurationPerTweet;
    }

    public long getAverageTotalDurationPerTweet()
    {
        return averageTotalDurationPerTweet;
    }

    public void setAverageTotalDurationPerTweet(long averageTotalDurationPerTweet)
    {
        this.averageTotalDurationPerTweet = averageTotalDurationPerTweet;
    }
}
