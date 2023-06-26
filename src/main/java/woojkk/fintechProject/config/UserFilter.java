package woojkk.fintechProject.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import woojkk.fintechProject.domain.UserVo;
import woojkk.fintechProject.service.AccountUserService;

@WebFilter(urlPatterns = "/account/*")
@RequiredArgsConstructor
public class UserFilter implements Filter {

  private final JwtAuthenticationProvider provider;
  private final AccountUserService accountUserService;

  @Override
  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader("X-AUTH-TOKEN");

    //토큰의 유효성을 검사 유효하지 않은 토큰인 경우, ServletException 발생
    if (!provider.validateToken(token)) {
      throw new ServletException("Invalid Access");
    }

    UserVo vo = provider.getUserVo(token);

    accountUserService.findByIdAndEmail(vo.getId(), vo.getEmail())
        .orElseThrow(() -> new ServletException("Invalid Access"));
    chain.doFilter(request, response);

  }
}
