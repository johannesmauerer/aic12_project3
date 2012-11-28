package aic12.project3.common.beans;

public class StatisticsBean
{
    private long averageDurationPerTweet;
    private long averageDurationPerRequest;
    private long maximumDurationOfRequest;
    private long minimumDurationOfRequest;

    public long getAverageDurationPerTweet()
    {
        return averageDurationPerTweet;
    }

    public void setAverageDurationPerTweet(long averageDurationPerTweet)
    {
        this.averageDurationPerTweet = averageDurationPerTweet;
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
}
