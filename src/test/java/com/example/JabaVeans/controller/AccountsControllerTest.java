package com.example.JabaVeans.controller;

import com.example.JabaVeans.dto.User;
import com.example.JabaVeans.service.UserDetailsImpl;
import com.example.JabaVeans.service.UserService;
import com.example.JabaVeans.viewhelper.PassProperties;
import com.example.JabaVeans.viewhelper.TablePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder encoder;

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void accountsGetRenders() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User("user", "user", true, "LVL1"));
        when(userService.findAllWhereRoleNotLike(anyInt(), anyInt(), eq("LVL99"))).thenReturn(new SliceImpl<>(users));
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("accounts-table"));
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void accountsPostRenders() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(new User("user", "user", true, "LVL1"));
        when(userService.findAllWhereRoleNotLike(anyInt(), anyInt(), eq("LVL99"))).thenReturn(new SliceImpl<>(users));


        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("tablePage", new TablePage()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("accounts-table"));
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void accountSwitchState_switchesState() throws Exception {
        User user = new User("user", "user", true, "LVL1");
        when(userService.findUser("user")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/state/{username}", "user"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertFalse(user.isEnabled());

    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void accountSwitchState_redirectsWhenUserNotFound() throws Exception {
        User user = new User("user", "user", true, "LVL1");
        when(userService.findUser("qqq")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/state/{username}", "user"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        assertTrue(user.isEnabled());

    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void addUser_RendersAndModelContainsBlankUser() throws Exception {

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andReturn()
                .getModelAndView();

        assertNull(null, ((User) mv.getModel().get("user")).getUsername());
        assertNull(null, ((User) mv.getModel().get("user")).getPassword());
        assertNull(null, ((User) mv.getModel().get("user")).getRole());
        assertFalse(((User) mv.getModel().get("user")).isEnabled());
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void addUser_hasErrors() throws Exception {
        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("user", new User())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andReturn()
                .getModelAndView();

        assertEquals("", mv.getModel().get("msg"));
        assertFalse((Boolean) mv.getModel().get("success"));
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void addValidUser_whenUserExists_doesNotAddUser() throws Exception {
        User user = new User("user", "user", true, "LVL1");

        when(userService.findUser("user")).thenReturn(user);


        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("user", user)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andReturn()
                .getModelAndView();


        assertFalse((Boolean) mv.getModel().get("success"));
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void addValidUser_whenUserDoesNotExist_addsUser() throws Exception {
        User user = new User("user", "user", true, "LVL1");

        when(userService.findUser("user")).thenReturn(null);


        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/add")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .flashAttr("user", user)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("account-add"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andReturn()
                .getModelAndView();


        assertTrue((Boolean) mv.getModel().get("success"));
    }

    @Test
    @WithMockUser(authorities = {"LVL99"})
    void changePassGet_rendersAndContainsBlankPassProperties() throws Exception {
        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/change-password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("change-password"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passProperties"))
                .andReturn()
                .getModelAndView();

        PassProperties passProperties = (PassProperties) mv.getModel().get("passProperties");
        assertEquals("", passProperties.getOldPass());
        assertEquals("", passProperties.getNewPass1());
        assertEquals("", passProperties.getNewPass2());
    }

    @Test
    void changePassPost_whenPassPropertiesHasErrors_returnsModelWithBlankPassProperties_And_DoesNotChangePassWord() throws Exception {
        User principal = new User("user", "user", true, "LVL99");
        when(userService.findUser("user")).thenReturn(principal);

        //PassProperties passProperties = new PassProperties("us","use","usae");
        PassProperties passProperties = new PassProperties();
        passProperties.setOldPass("us");
        passProperties.setNewPass1("use");
        passProperties.setNewPass2("usae");
        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/change-password")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsImpl(principal)))
                .param("oldPass", passProperties.getOldPass())
                .param("newPass1", passProperties.getNewPass1())
                .param("newPass2", passProperties.getNewPass2())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().errorCount(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passProperties"))
                .andExpect(MockMvcResultMatchers.view().name("change-password"))
                .andReturn().getModelAndView();


        assertEquals("user", principal.getPassword());

        assertEquals("", ((PassProperties) mv.getModel().get("passProperties")).getOldPass());

    }

    @Test
    void changePassPost_whenPassPropertiesHasNoErrors_returnsModelWithPassProperties_And_ChangesPassword() throws Exception {
        User principal = new User("user", "user", true, "LVL99");

        PassProperties passProperties = new PassProperties();
        passProperties.setOldPass("user");
        passProperties.setNewPass1("zaq1");
        passProperties.setNewPass2("zaq1");

        when(userService.findUser("user")).thenReturn(principal);
        when(encoder.encode(passProperties.getNewPass1())).thenReturn(passProperties.getNewPass1());
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        ModelAndView mv = mockMvc.perform(MockMvcRequestBuilders.post("/accounts/change-password")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(new UserDetailsImpl(principal)))
                .param("oldPass", passProperties.getOldPass())
                .param("newPass1", passProperties.getNewPass1())
                .param("newPass2", passProperties.getNewPass2())
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("passProperties"))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.view().name("change-password"))
                .andReturn().getModelAndView();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(encoder, times(1)).encode(passProperties.getNewPass1());
        verify(userService, times(1)).saveUser(userCaptor.capture());

        assertEquals(passProperties.getNewPass1(), userCaptor.getValue().getPassword());
        assertEquals(passProperties.getNewPass1(), principal.getPassword());
        assertEquals("", ((PassProperties) mv.getModel().get("passProperties")).getOldPass());
    }


}