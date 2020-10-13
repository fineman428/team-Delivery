package rentalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import rentalService.config.kafka.KafkaProcessor;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    DeliveryRepository DeliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRentaled_Delivery(@Payload Rentaled rentaled){

        if(rentaled.isMe()){
            Delivery delivery = new Delivery();
            delivery.setRentalId(rentaled.getId());
            delivery.setStatus("DELIVERED");
            delivery.setQty(rentaled.getQty());

            DeliveryRepository.save(delivery);
        }
    }

}
