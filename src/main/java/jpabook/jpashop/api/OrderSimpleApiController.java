package jpabook.jpashop.api;


import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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


}