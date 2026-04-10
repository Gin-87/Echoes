package echos.CharacterService.Controller;

import echos.AuthenticationService.Util.JwtUtil;
import echos.CharacterService.Entity.Character;
import echos.CharacterService.Service.UserFavoriteService;

import echos.Common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户收藏管理")
@RestController
@RequestMapping("/api/user/favorites")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private JwtUtil jwtUtil;

    // 根据用户 Token 查询收藏的角色
    @Operation(summary = "获取当前用户的所有收藏角色")
    @GetMapping("/get_all_favorite")
    public ApiResponse<List<Character>> getUserFavorites(
            @RequestHeader("Authorization") String userToken) {
        try {
            // 从 token 中提取 userId
            Long userId = jwtUtil.getUserIdFromToken(userToken);

            List<Character> favorites = userFavoriteService.getFavoritesByUserId(userId);
            return ApiResponse.success(favorites);
        } catch (Exception e) {
            return ApiResponse.error(401, "Authorization failed: " + e.getMessage());
        }
    }

    // 添加收藏
    @Operation(summary = "添加收藏角色")
    @PostMapping("/add_favorite/{characterId}")
    public ApiResponse<String> addFavorite(
            @RequestHeader("Authorization") String userToken,
            @PathVariable(name = "characterId") Long characterId) {

        System.out.println("Received characterId: " + characterId + ", type: "
                + (characterId != null ? characterId.getClass() : "null"));

        try {
            Long userId = jwtUtil.getUserIdFromToken(userToken);
            System.out.println("Extracted userId: " + userId);

            userFavoriteService.addFavorite(userId, characterId);
            return ApiResponse.success("Character has been added to favorites");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(400, "Failed to add character to favorites: " + e.getMessage());
        }
    }

    // 取消收藏
    @Operation(summary = "取消收藏角色")
    @DeleteMapping("/remove_favorite/{characterId}")
    public ApiResponse<String> removeFavorite(
            @RequestHeader("Authorization") String userToken,
            @PathVariable(name = "characterId") Long characterId) {

        // 从 token 中提取 userId
        Long userId = jwtUtil.getUserIdFromToken(userToken);

        if (userFavoriteService.removeFavorite(userId, characterId)) {
            return ApiResponse.success("Character has been removed from favorites");
        } else {
            return ApiResponse.error(400, "Favorite does not exist");
        }
    }

}
