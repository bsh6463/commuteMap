package hello.commute.api;

import hello.commute.api.dto.Path;
import hello.commute.api.dto.SearchRouteRes;
import hello.commute.api.dto.SubPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ApiController.class)
@AutoConfigureWebMvc
class ApiControllerTest {

    @MockBean OdSayClient odSayClient;
    @Autowired MockMvc mockMvc;

    @Test
    void searchRoute() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("SX","126.9707979959352");
        params.add("SY","37.5547020732267");
        params.add("EX","127.10012275846414");
        params.add("EY","37.513264531390575");

        ModelAndView mav;
        mav = mockMvc.perform(MockMvcRequestBuilders.post("/search").params(params)).andReturn().getModelAndView();
        assert mav != null;
        SearchRouteRes result = (SearchRouteRes) mav.getModel().get("result");
        Path path = result.getPathList().get(0);
        SubPath subPath = path.getSubPathList().get(0);
        System.out.println(subPath.getTrafficType());
    }
}