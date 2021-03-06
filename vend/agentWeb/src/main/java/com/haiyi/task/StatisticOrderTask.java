package com.haiyi.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.haiyi.domain.OrderAccount;
import com.haiyi.domain.vo.OrderMonthStasticVO;
import com.haiyi.service.ComsumeLogService;
import com.haiyi.service.OrderAccountService;
import com.maizi.utils.DateUtil;

/**
 * 订单月统计任务
 * @author Administrator
 *
 */
public class StatisticOrderTask {
	
	@Autowired
	ComsumeLogService comsumeLogService;
	
	@Autowired
	OrderAccountService orderAcccountService;
	 
	public void monthStatistic(){ 
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH,-1);
		String date = DateUtil.dateToString(calendar.getTime(),"yyyy-MM");
		
		List<OrderMonthStasticVO> orderMonthStasticVOs = comsumeLogService.queryMonthStatistic(date);
		
		List<OrderAccount> orderAccounts = new ArrayList<OrderAccount>();
		 
		OrderAccount orderAccount = null;
		int size=orderMonthStasticVOs.size();
		
		for(int i=0;i<size;i++){
			OrderMonthStasticVO orderMonthStasticVO=orderMonthStasticVOs.get(i);
			if(i==0){
				orderAccount=new OrderAccount();
				//支付成功状态
				if("2".equals(orderMonthStasticVO.getPayStatus())){
					orderAccount.setFinishCount(orderMonthStasticVO.getCount());
				}
				//支付失败
				if("4".equals(orderMonthStasticVO.getPayStatus())){
					orderAccount.setErrorCount(orderMonthStasticVO.getCount());
				}
				orderAccount.setCreateDate(date);
				if(i!=size-1){
					continue;
				}else{
					//只有一条记录时将数据添加到集合中
					orderAccounts.add(orderAccount);
					break;
				}
			}

			if(i==size-1){
				//将最后一条记录添加到数据库
				orderAccounts.add(orderAccount);
			}
		}
		//将包装好的数据添加到数据库
		for(OrderAccount orderAccount2:orderAccounts){
			orderAcccountService.add(orderAccount2);
		}
	}
}