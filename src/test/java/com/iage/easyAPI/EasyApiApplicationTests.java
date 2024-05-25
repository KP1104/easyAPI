package com.iage.easyAPI;

import com.iage.easyAPI.entity.EncMobiShopperTrnItems;
import com.iage.easyAPI.model.EncMobiShopperTrnItemsModel;
import com.iage.easyAPI.service.EncMobiShopperTrnItemsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EasyApiApplicationTests {

	@Autowired
	EncMobiShopperTrnItemsService encMobiShopperTrnItemsService;

	@Test
	void contextLoads() {
	}

//	@Test
//	public void testMethod() {
//		List<EncMobiShopperTrnItemsModel> list = encMobiShopperTrnItemsService.getTranCdAndItemCount("0001", "0010", "test", "kp");
//		if(list.isEmpty()) {
//			System.out.println("No data found");
//		} else {
//			for (EncMobiShopperTrnItemsModel item :
//					list) {
//				String tranCd = item.getTranCd();
//				Integer itemCount = item.getItemCount();
//				System.out.println(tranCd + itemCount);
//			}
//		}
//
//	}

}
