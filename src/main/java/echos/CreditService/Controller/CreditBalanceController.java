package echos.CreditService.Controller;

import echos.CreditService.Entity.UserCreditRecord;
import echos.CreditService.Service.CreditBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@Tag(name = "积分管理", description = "用于管理用户积分的 API 接口")
public class CreditBalanceController {

    @Autowired
    private CreditBalanceService creditBalanceService;

    /**
     * 获取用户积分余额
     * @param userId 用户ID
     * @return 用户当前积分余额
     */
    @GetMapping("/balance")
    @Operation(summary = "获取用户积分余额", description = "获取指定用户的当前积分余额。")
    public ResponseEntity<Integer> getUserBalance(@RequestParam Long userId) {
        return ResponseEntity.ok(creditBalanceService.getUserBalance(userId));
    }

    /**
     * 更新用户积分余额
     * @param userId 用户ID
     * @param changeAmount 变动积分值
     * @param description 变动描述
     * @return 无返回值
     */
    @PostMapping("/update")
    @Operation(summary = "更新用户积分余额", description = "更新用户积分余额，并记录变动详情。")
    public ResponseEntity<Void> updateUserBalance(@RequestParam Long userId,
                                                  @RequestParam Integer changeAmount,
                                                  @RequestParam String description) {
        creditBalanceService.updateUserBalance(userId, changeAmount, description);
        return ResponseEntity.ok().build();
    }

    /**
     * 初始化用户积分
     * @param userId 用户ID
     * @return 无返回值
     */
    @PostMapping("/initialize")
    @Operation(summary = "初始化用户积分", description = "初始化用户积分余额为 0。")
    public ResponseEntity<Void> initializeUserCredit(@RequestParam Long userId) {
        creditBalanceService.initialUserCredit(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取用户积分变动记录
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页记录数
     * @return 用户积分变动记录列表
     */
    @GetMapping("/records")
    @Operation(summary = "获取用户积分变动记录", description = "分页获取用户的积分变动记录。")
    public ResponseEntity<List<UserCreditRecord>> getUserCreditRecords(@RequestParam Long userId,
                                                                       @RequestParam int page,
                                                                       @RequestParam int size) {
        return ResponseEntity.ok(creditBalanceService.getUserCreditRecords(userId, page, size));
    }

    /**
     * 检查用户积分是否充足
     * @param userId 用户ID
     * @param requiredAmount 所需积分
     * @return 是否积分充足
     */
    @GetMapping("/check-balance")
    @Operation(summary = "检查积分是否充足", description = "检查用户当前积分是否满足所需积分。")
    public ResponseEntity<Boolean> checkBalanceSufficient(@RequestParam Long userId,
                                                          @RequestParam Integer requiredAmount) {
        return ResponseEntity.ok(creditBalanceService.checkBalanceSufficient(userId, requiredAmount));
    }
}
