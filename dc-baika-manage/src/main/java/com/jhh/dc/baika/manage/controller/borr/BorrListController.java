package com.jhh.dc.baika.manage.controller.borr;

import com.jhh.dc.baika.manage.service.borr.BorrListService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jhh.dc.baika.manage.entity.Response;


@Controller
public class BorrListController {

	@Autowired
	private BorrListService borrListService;

	//获取合同
	@ResponseBody
	@RequestMapping(value = "/borrList/{perId}", method = RequestMethod.GET)
	public Response getBorrByPerId(@PathVariable Integer perId) throws Exception {
		Response response = borrListService.getBorrByPerId(perId);
		return response;
	}

	/**
	 *
	 *	取消合同
	 * @Deprecated
	 * @see
	 */
	@ResponseBody
	@RequestMapping(value = "/borrList/id", method = RequestMethod.POST)
	public Response saveBorrList(@RequestParam Integer id) throws Exception {
		Response response = borrListService.cancelBorrList(id);
		return response;
	}

	/**
	 * 取消合同
	 * @param borrowIds 合同Id集，逗号分隔
	 */
	@RequestMapping(value = "/cancel/borrList", method = RequestMethod.POST)
    @ResponseBody
	public Response cancelBorrList(String borrowIds){
		Response resp = null;
		if (StringUtils.isBlank(borrowIds)){
			resp = new Response();
			resp.setCode(201);
			resp.setMsg("合同Id不能为空");
			resp.setData(null);
			return resp;
		}

		String[] borrIds = borrowIds.split(",");
		for(String borrId : borrIds){
			if(StringUtils.isNotBlank(borrId)){
				resp = borrListService.cancelBorrList(Integer.parseInt(borrId));
			}
		}

		return resp;
	}

	//分单特殊
	@ResponseBody
	@RequestMapping(value = "/borrList/submenuTransfer", method = RequestMethod.GET)
	public String submenuTransfer() throws Exception {
		int count = borrListService.submenuTransfer();
		return "成功" + count +"条";
	}
}
