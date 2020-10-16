package rentalService;

import lombok.Data;

@Data
public class ProductInventoryRegisted extends AbstractEvent {

    private Long id;
    private String name;
    private int qty ;

    public ProductInventoryRegisted(){
        super();
    }
/*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }*/
}
