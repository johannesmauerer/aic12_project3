package aic12.project3.common.beans;

import java.util.List;

public class CompanyList {
private List<String> list;
    
    public CompanyList() {}
    
    public CompanyList(List<String> list)
    {
        this.list = list;
    }

    public List<String> getList()
    {
        return list;
    }

    public void setList(List<String> list)
    {
        this.list = list;
    }
}
