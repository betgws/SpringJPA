package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiContoller {

    private final MemberService memberService;




    @PutMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
       Long id = memberService.join(member);
       return new CreateMemberResponse(id);
    }



    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream().map(m->new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }


    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data; // 한번 감싸서 내보냈는데 왜 data 이거 왜생긴거임
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }
}


