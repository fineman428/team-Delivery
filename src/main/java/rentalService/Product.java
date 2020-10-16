package rentalService;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Data
@Entity
@Table(name="Product_table")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long productId;
    private String name;
    private int qty ;

    @PostPersist
    public void onPostPersist(){

        ProductInventoryRegisted productInventoryRegisted = new ProductInventoryRegisted();
        BeanUtils.copyProperties(this, productInventoryRegisted);
        productInventoryRegisted.publishAfterCommit();

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
    }
    */

}
