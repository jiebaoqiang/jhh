package com.jhh.dc.baika.manage.controller.risk;

import com.jhh.dc.baika.manage.controller.BaseController;
import com.jhh.dc.baika.entity.common.Constants;
import com.jhh.dc.baika.manage.entity.Response;
import com.jhh.dc.baika.manage.service.risk.ReviewService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/review")
public class ManualReview extends BaseController {

	@Autowired
	private ReviewService reviewService;

	// 审核管理通过
	@ResponseBody
	@RequestMapping(value = "/contract/pass", method = RequestMethod.POST)
	public Response pass(@RequestParam Integer borrId,
					   @RequestParam String userNum) throws Exception {
		Response response = reviewService.saveManuallyReview(borrId, "", userNum, Constants.OperationType.MANUALLY_PASS);
		return response;
	}

	// 审核管理拒绝
	@ResponseBody
	@RequestMapping(value = "/contract/reject", method = RequestMethod.POST)
	public Response reject(@RequestParam Integer borrId,
						 @RequestParam String userNum,
						 @RequestParam String reason) throws Exception {
		Response response = reviewService.saveManuallyReview(borrId, reason, userNum, Constants.OperationType.MANUALLY_REJECT);

		return response;
	}

	//  拉黑并拒绝
	@ResponseBody
	@RequestMapping(value = "/contract/blackReject", method = RequestMethod.POST)
	public Response contractBlackRejec(@RequestParam Integer borrId,
								   @RequestParam String userNum,
								   @RequestParam String reason) throws Exception {
		Response response = reviewService.saveManuallyReview(borrId, reason, userNum, Constants.OperationType.BLACK_REJECT);

		return response;
	}
	//  审核管理取消
	@ResponseBody
	@RequestMapping(value = "/contract/cancel", method = RequestMethod.POST)
	public Response contractCancel(@RequestParam String borrId,
								   @RequestParam String userNum,
								   @RequestParam String reason) throws Exception {
		Response response = reviewService.cancel(borrId, reason, userNum);

		return response;
	}
	//  洗白
	@ResponseBody
	@RequestMapping(value = "/contract/white", method = RequestMethod.POST)
	public Response contractWhite(@RequestParam Integer borrId,
								  @RequestParam String userNum,
								  @RequestParam String reason) throws Exception {
		Response response = reviewService.saveManuallyReview(borrId, reason, userNum, Constants.OperationType.WHITE);

		return response;
	}
	//  拉黑
	@ResponseBody
	@RequestMapping(value = "/contract/black", method = RequestMethod.POST)
	public Response contractBlack(@RequestParam Integer borrId,
								  @RequestParam String userNum,
								  @RequestParam String reason) throws Exception {
		Response response = reviewService.saveManuallyReview(borrId, reason, userNum, Constants.OperationType.MANUALLY_BLACK);

		return response;
	}

	//  转件
	@ResponseBody
	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public Response transfer(@RequestParam String borrIds,
								   @RequestParam String userNum) throws Exception {
		return reviewService.transfer(borrIds, userNum);
	}

	//  放款
	@ResponseBody
	@RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Response pay(@RequestParam Integer borrId, @RequestParam String userNum, @RequestParam(required = false) String payChannel) throws Exception {
        return reviewService.pay(borrId, userNum, payChannel);
    }
}
