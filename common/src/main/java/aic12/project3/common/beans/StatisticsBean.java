package aic12.project3.common.beans;

public class StatisticsBean
{
    private double averageProcessingDurationPerTweet;
    private double averageTotalDurationPerTweet;
    private double averageDurationPerRequest;
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

    public double getAverageDurationPerRequest()
    {
        return averageDurationPerRequest;
    }

    public void setAverageDurationPerRequest(double averageDurationPerRequest)
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

    public double getAverageProcessingDurationPerTweet()
    {
        return averageProcessingDurationPerTweet;
    }

    public void setAverageProcessingDurationPerTweet(double averageProcessingDurationPerTweet)
    {
        this.averageProcessingDurationPerTweet = averageProcessingDurationPerTweet;
    }

    public double getAverageTotalDurationPerTweet()
    {
        return averageTotalDurationPerTweet;
    }

    public void setAverageTotalDurationPerTweet(double averageTotalDurationPerTweet)
    {
        this.averageTotalDurationPerTweet = averageTotalDurationPerTweet;
    }
}
