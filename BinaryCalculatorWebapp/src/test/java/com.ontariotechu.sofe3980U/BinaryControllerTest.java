package com.ontariotechu.sofe3980U;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BinaryController.class)
public class BinaryControllerTest {

    @Autowired
    private MockMvc mvc;

   
    @Test
    public void getDefault() throws Exception {
        this.mvc.perform(get("/"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", ""))
			.andExpect(model().attribute("operand1Focused", false));
    }

	    @Test
    public void getParameter() throws Exception {
        this.mvc.perform(get("/").param("operand1","111"))
            .andExpect(status().isOk())
            .andExpect(view().name("calculator"))
			.andExpect(model().attribute("operand1", "111"))
			.andExpect(model().attribute("operand1Focused", true));
    }
	@Test
	    public void postParameter() throws Exception {
        this.mvc.perform(post("/").param("operand1","111").param("operator","+").param("operand2","111"))//.andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("result"))
			.andExpect(model().attribute("result", "1110"))
			.andExpect(model().attribute("operand1", "111"));
    }
    @Test
    public void postInvalidBinaryValues() throws Exception {
        this.mvc.perform(post("/").param("operand1", "xyz").param("operator", "+").param("operand2", "111"))
                .andExpect(status().isOk())
                .andExpect(view().name(is("error")))
                .andExpect(model().attribute("errorMessage", "Invalid binary value(s)"));
    }

    @Test
    public void postInvalidOperator() throws Exception {
        this.mvc.perform(post("/").param("operand1", "101").param("operator", "&").param("operand2", "110"))
                .andExpect(status().isOk())
                .andExpect(view().name(is("error")))
                .andExpect(model().attribute("errorMessage", "Invalid operator"));
    }

    @Test
    public void postMissingOperand() throws Exception {
        this.mvc.perform(post("/").param("operand1", "101").param("operator", "*")) // Missing operand2
                .andExpect(status().isOk())
                .andExpect(view().name(is("error")))
                .andExpect(model().attribute("errorMessage", "Missing operand"));
    }

}