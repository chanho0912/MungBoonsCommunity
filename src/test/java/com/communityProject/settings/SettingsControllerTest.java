package com.communityProject.settings;

import com.communityProject.account.AccountRepository;
import com.communityProject.account.AccountService;
import com.communityProject.domain.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;
    @AfterEach
    public void afterEach() {
        accountRepository.deleteAll();
    }

    @WithAccount("chanho")
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception {
        mockMvc.perform(get("/settings/profile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("chanho")
    @DisplayName("프로필 수정 하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개를 수정하는 경우.";
        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));
        Account chanho = accountRepository.findByNickname("chanho");
        assertEquals(bio, chanho.getBio());
    }

    @WithAccount("chanho")
    @DisplayName("프로필 수정 하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception {
        String bio = "길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우. 길게 소개를 수정하는 경우.";
        mockMvc.perform(post("/settings/profile")
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account chanho = accountRepository.findByNickname("chanho");
        assertNull(chanho.getBio());
    }

    @WithAccount("chanho")
    @DisplayName("패스워드 수정하기 - 입력 폼")
    @Test
    void updatePasswordFormTest() throws Exception {
        mockMvc.perform(get("/settings/password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("chanho")
    @DisplayName("패스워드 수정하기 - 입력값 정상")
    @Test
    void updatePasswordWithProperValTest() throws Exception {
        String updatePassword="33334444";
        mockMvc.perform(post("/settings/password")
                .with(csrf())
                .param("newPassword", updatePassword).param("newPasswordConfirm", updatePassword))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/password"))
                .andExpect(flash().attributeExists("message"));

        Account testAccount = accountRepository.findByNickname("chanho");
        assertTrue(passwordEncoder.matches(updatePassword, testAccount.getPassword()));
    }

    @WithAccount("chanho")
    @DisplayName("패스워드 수정하기 - 패스워드 불일치")
    @Test
    void updatePasswordWithNonProperValTest() throws Exception {
        String updatePassword="33334444";
        mockMvc.perform(post("/settings/password")
                .with(csrf())
                .param("newPassword", updatePassword).param("newPasswordConfirm", "44444444"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/password"))
                .andExpect(model().hasErrors());
    }

    @WithAccount("chanho")
    @DisplayName("닉네임 수정 폼")
    @Test
    void updateAccountForm() throws Exception {
        mockMvc.perform(get("/settings/account"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @WithAccount("chanho")
    @DisplayName("닉네임 수정하기 - 입력 값 정상")
    @Test
    void updateAccountNicknameWithProperInput() throws Exception {
        String updateNickname = "abkdaswef";
        mockMvc.perform(post("/settings/account")
                .param("nickname", updateNickname).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/account"))
                .andExpect(flash().attributeExists("message"));

        assertNotNull(accountRepository.findByNickname(updateNickname));
    }

    @WithAccount("chanho")
    @DisplayName("닉네임 수정하기 - 입력 값 에러")
    @Test
    void updateAccountNicknameWithInvalidInput() throws Exception {
        String updateNickname = "''''TQWE1";
        mockMvc.perform(post("/settings/account")
                .param("nickname", updateNickname).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("settings/account"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

}