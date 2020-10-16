package rentalService;

import javax.persistence.*;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Data
@Entity
@Table(name="Delivery_table")
public class Delivery {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long rentalId;
    private int qty;
    private String status;

    @PostPersist
    public void onPostPersist(){
        Delivered delivered = new Delivered();
        BeanUtils.copyProperties(this, delivered);
        delivered.publishAfterCommit();

        if (status.equals("CANCELED_UnregisteredProduct") ||
                status.equals("CANCELED_OutOfStock") ){
            // 미등록 상품 = 수량 부족 이벤트 임시로 같이 사용
            OutOfStockRentalCanceled outOfStockRentalCanceled = new OutOfStockRentalCanceled();
            BeanUtils.copyProperties(this, outOfStockRentalCanceled);
            //outOfStockRentalCanceled.setRentalId(rentaled.getId());
            //outOfStockRentalCanceled.setStatus("CANCELED");
            outOfStockRentalCanceled.publishAfterCommit();

        }
    }

    @PostUpdate
    public void onPUUpdate(){

        if (status.equals("CANCELED")) {

        }

    }

    @PostRemove
    public void onPostRemove(){
        DeliveryCanceled deliveryCanceled = new DeliveryCanceled();
        BeanUtils.copyProperties(this, deliveryCanceled);
        deliveryCanceled.publishAfterCommit();


    }

    /*
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }*/
}
