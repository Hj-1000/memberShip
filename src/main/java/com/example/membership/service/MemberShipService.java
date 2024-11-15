package com.example.membership.service;

import com.example.membership.constant.Role;
import com.example.membership.dto.MemberShipDTO;
import com.example.membership.entity.MemberShip;
import com.example.membership.repository.MemberShipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberShipService implements UserDetailsService {


    private final MemberShipRepository memberShipRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    // 회원가입 : 컨트롤러에서 dto를 입력받아 entity로 변환하여 repository의
    // save 를 이용해서 저장한다.
    // 반환값은 dto 전체를 반환으로 하자
    // 그런데 ~~ 만약에 회원이 이미 가입되어 있다면?

    public MemberShipDTO signUp(MemberShipDTO memberShipDTO){

        // 사용자가 이미 있는지 확인
        // 가입하려는 email 로 이미 사용자가 가입이 되어있는지 확인하다.

        MemberShip memberShip =
        memberShipRepository.findByEmail(memberShipDTO.getEmail());

        if (memberShip != null){ // 확인했더니 이미 가입이 되어있다면
            throw new IllegalStateException("이미 가입된 회원입니다");
        }

        memberShip =
        modelMapper.map(memberShipDTO, MemberShip.class);

        // 일반유저
        memberShip.setRole(Role.ADMIN);
        // 비밀번호를 암호화해서 저장한다
        memberShip.setPassword(passwordEncoder.encode( memberShipDTO.getPassword()));

        memberShip =
        memberShipRepository.save(memberShip);      // 저장

        return  modelMapper.map(memberShip, MemberShipDTO.class);
    }
    // 로그인을 만든다 // userDetailsService를 구현새서 사용한다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 사용자가 입력한 email을 가지고 db에서 검색한다.
        // 그러면 email과 password를 포함한 entity를 받을것이다.
        // 로그인은 기본적으로 email 과 password를 찾거나
        // email로 검색해서 가져온 데이터가 null이 아니라면
        // 그 안에 있는 password를 가지고
        // 비교해서 맞다면 로그인한다.
        log.info("유저디테일 서비스로 들어온 이메일 : " + email);
        MemberShip memberShip =
        this.memberShipRepository.findByEmail(email);
        // 이메일로 검색해서 가져온 값이 없다면 , 그러니까 회원가입이 되어있지 않다면
        // try catch 문으로 다른 화면으로 달린 메시지를 가지고 로그인창으로 보내던
        // 컨트롤러 창에서 알아서 처리하게함
        if (memberShip == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
        // 예외처리가 되지 않았아면
        log.info("현재 찾은 회원정보 : " + memberShip);

        //권한 처리
        String role = "";
        if ("ADMIN".equals(memberShip.getRole().name())){
            log.info("관리자");
            role = Role.ADMIN.name();
        }else  {
            log.info("일반유저");
            role = Role.USER.name();
        }
        return User.builder()
                .username(memberShip.getEmail())
                .password(memberShip.getPassword()) //
                .roles(role)
                .build();
    }








}
