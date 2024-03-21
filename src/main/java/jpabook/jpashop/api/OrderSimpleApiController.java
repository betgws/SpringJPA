package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/** XtoOne 관계일때 성능 최적화*/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); // 주문 다 들고옴
        for(Order order : all){
            order.getMember().getName(); //Lazy 강제 초기화 프록시 객체에서 이제 진짜 객체 가져옴
            order.getDelivery().getAddress();
        }
        return all;
    }// 이렇게만 하면 무한 루프에 빠짐


    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){

        //Order 2개일때 5번이나 쿼리가 나감
        //N+1 -> 1+ 회원N+ 배송N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o->new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;

    }
    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화 영속성 컨테스트에 없어서 DB에서 찾아봄 -> 쿼리 날림ㅌ
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getMember().getAddress(); // LAZY 초기화
        }
    }


}