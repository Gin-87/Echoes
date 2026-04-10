package echos.SystemService;

import echos.AuthenticationService.Util.JwtUtil;
import echos.AuthorizationService.Service.RoleToActionService;
import echos.AuthorizationService.Service.RoleToMenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private RoleToMenuService roleToMenuService;

    @Autowired
    private RoleToActionService roleToActionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String resource = request.getRequestURI(); // 获取请求路径
        System.out.println("拦截器执行: 请求路径 = " + request.getRequestURI());


//        // 从请求头中获取 Token
//        String token = request.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Unauthorized: Missing or invalid token");
//            return false;
//        }
//
//        Long userRole = jwtUtil.getRoleIdFromToken(token); // 从 Token 获取用户角色
//
//        // 判断是否是菜单请求
//        if (isMenuRequest(resource)) {
//            // 校验菜单权限
//            if (!roleToMenuService.checkMenuPermission(userRole, resource)) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("Forbidden: No menu access permission");
//                return false; // 拦截请求
//            }
//        } else {
//            // 校验操作权限
//            if (!roleToActionService.checkActionPermission(userRole, resource)) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("Forbidden: No action access permission");
//                return false; // 拦截请求
//            }
//
//            //todo 加ABAC级鉴权
////            if (false){
////                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
////                response.getWriter().write("Unauthorized: Unauthorized");
////                return false;
////            }
//        }

        return true; // 放行请求
    }

//    private boolean isMenuRequest(String resource) {
//        // 判断资源是否属于菜单权限（如前缀判断、路径规则等）
//        return resource.startsWith("/menu"); // 示例：所有 /menu 开头的路径属于菜单权限
//    }

}
