package rentalService;


import org.springframework.beans.BeanUtils;
import rentalService.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository DeliveryRepository;
    @Autowired
    ProductRepository ProductRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentaled_Delivery(@Payload Rentaled rentaled){

        if(rentaled.isMe()){

            // 배송 등록
            Delivery delivery = new Delivery();
            delivery.setProductId(rentaled.getProductId());
            delivery.setRentalId(rentaled.getId());
            delivery.setStatus("DELIVERED");
            delivery.setQty(rentaled.getQty());

            // 재고 확인
            Optional<Product> productOptional = ProductRepository.findByProductId(rentaled.getProductId());
            Product product = null;

            try {

                product = productOptional.get();

            } catch (Exception e) {

                // 상품정보 확인 불가
                System.out.println("rentaled 수신 : 상품정보 확인불가");

                delivery.setStatus("CANCELED_UnregisteredProduct");
                DeliveryRepository.save(delivery);
                return;
            }

            if ( product.getQty() < rentaled.getQty() ){
                // 재고 부족 -> 보상 트랜젝션 (saga pattern)
                System.out.println("재고 수량 비교 : qty="+product.getQty()+" / rentaled.getQty()="+rentaled.getQty());
                System.out.println("rentaled 수신 : 재고 부족 -> 보상 트랜젝션 (saga pattern))");

                delivery.setStatus("CANCELED_OutOfStock");

            } else {
                // 정상 - 재고 차감
                System.out.println("rentaled 수신 : 정상 - 재고 차감");
                product.setQty(  product.getQty()  -  rentaled.getQty() );
                ProductRepository.save(product);
            }

            // 배송정보 저장
            DeliveryRepository.save(delivery);
            
        }
    }
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverProductSaved_Product(@Payload ProductSaved productSaved){

        if(productSaved.isMe()){

            Product product = new Product();
            BeanUtils.copyProperties(productSaved, product);
            product.setProductId(productSaved.getId());
            //product.setName( productSaved.getName());

            ProductRepository.save(product);
        }
    }

}
