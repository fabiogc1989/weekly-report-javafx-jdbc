package model.dto;

public class SearchModel<T> {
    private T model;
    
    private int page;
    
    public SearchModel(){
    }
    
    public SearchModel(T model){
        this.model = model;
    }
    
    public SearchModel(T model, int page){
        this.model = model;
        this.page = page;
    }

    public T getModel() {
        return model;
    }
    
    public void setModel(T model){
        this.model = model;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
