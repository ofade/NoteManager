package app;


import app.controller.LoginController;
import app.login.LoginBean;
import app.login.LoginDelegate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {NoteManagerMainApp.class})
public class LoginControllerIntegrationTest extends IntegrationTest{

	@Mock
	LoginDelegate loginDelegate;

	@Mock
	LogoutHandler logoutHandler;

	@InjectMocks
	LoginController loginController;


	@Before
	public void setup() {
		super.setup();
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).setViewResolvers(viewResolver).build();
	}

	@Test
	public void testDisplayLogin() throws Exception {
		 this.mockMvc.perform(get("/login"))
		            .andExpect(status().isOk())
				 	.andExpect(model().attribute("loginBean", new LoginBean()))
		            .andExpect(view().name("login"));
	}

	@Test
	public void testDisplayRegistration() throws Exception {
		this.mockMvc.perform(get("/register"))
		            .andExpect(status().isOk())
					.andExpect(model().attribute("user", new LoginBean()))
		            .andExpect(view().name("register"));
	}

	@Test
	public void testRegisterWithValidDetails() throws Exception{
		LoginBean loginBean = getLoginBean();
		Mockito.when(loginDelegate.usernameExists("username")).thenReturn(false);


		this.mockMvc.perform(post("/register")
				.param("username", loginBean.getUsername())
				.param("password", loginBean.getPassword()))
			.andExpect(status().isOk())
			.andExpect(view().name("home"));
	}

	@Test
	public void testRegisterWithMissingDetails() throws Exception{
		this.mockMvc.perform(post("/register")
				.param("user", "name"))
				.andExpect(status().isOk())
				.andExpect(view().name("register"));
	}

	@Test
	public void testRegisterWithUsernameThatIsAlreadyTaken() throws Exception{
		LoginBean loginBean = getLoginBean();
		Mockito.when(loginDelegate.usernameExists("username")).thenReturn(true);


		this.mockMvc.perform(post("/register")
				.param("username", loginBean.getUsername())
				.param("password", loginBean.getPassword())
	               )
			.andExpect(status().isOk())
			.andExpect(view().name("register"));
	}

	@Test
	public void testGetHome() throws Exception {
		 this.mockMvc.perform(get("/home"))
		 	.andExpect(status().isOk())
		 	.andExpect(view().name("home"));
	}

	@Test
	public void testLogout() throws Exception {
		doNothing().when(logoutHandler).logout(any(), any(), any());
		 this.mockMvc.perform(get("/logout"))
		 	.andExpect(status().is(302))
		 	.andExpect(redirectedUrl("/home"));
	}

	@Test
	public void testExecuteLoginUserDoesNotExist() throws Exception{
		LoginBean loginBean = getLoginBean();

		mockMvc.perform(post("/login")
				.param("username", loginBean.getUsername())
				.param("password", loginBean.getPassword())
	               )
			.andExpect(status().isMethodNotAllowed());
	}

	private LoginBean getLoginBean() {
		LoginBean loginBean = new LoginBean("username", "password");
		return loginBean;
	}
}
