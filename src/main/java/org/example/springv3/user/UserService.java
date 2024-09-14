package org.example.springv3.user;

import lombok.RequiredArgsConstructor;
import org.example.springv3.core.error.ex.Exception401;
import org.example.springv3.core.error.ex.Exception400;
import org.example.springv3.core.error.ex.Exception404;
import org.example.springv3.core.error.ex.Exception500;
import org.example.springv3.core.util.MyFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void 프로필업로드(MultipartFile profile, User sessionUser) {
        String imageFileName = MyFile.파일저장(profile);

        // DB에 저장
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없어요"));
        userPS.setProfile(imageFileName);
    } // 더티체킹 update됨



    public String 프로필사진가져오기(User sessionUser) {
        User userPS = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없어요"));

//        String profile = userPS.getProfile() == null ? "nobody"
//        return userPS.getProfile()
        String profile = userPS.getProfile() == null ? "nobody.png" : userPS.getProfile();
        return profile;
    }

    public User 로그인(UserRequest.LoginDTO loginDTO) {
        User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
        return user;
    }

    @Transactional
    public void 회원가입(UserRequest.JoinDTO joinDTO) {

        Optional<User> userOp= userRepository.findByUsername(joinDTO.getUsername());
        if(userOp.isPresent()) {
            throw new Exception400("이미 존재하는 유저입니다.");
        }


        userRepository.save(joinDTO.toEntity());

    }

    public boolean 유저네임중복되었니(String username) {
        // Optional 로 리턴
        Optional<User> userOP = userRepository.findByUsername(username); // 무조건 throw 날리면 안됨! 이건 중복됐을 수도 있고 안됐을 수도 있으니까
        if(userOP.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
