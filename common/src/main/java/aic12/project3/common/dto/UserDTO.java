package aic12.project3.common.dto;

public class UserDTO
{
    private String userName;
    private String companyName;
    private String pwHash;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getPwHash()
    {
        return pwHash;
    }

    public void setPwHash(String pwHash)
    {
        this.pwHash = pwHash;
    }
}
