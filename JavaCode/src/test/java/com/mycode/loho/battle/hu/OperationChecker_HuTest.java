package com.mycode.loho.battle.hu;

import com.kodgames.CheckWin;
import com.kodgames.battlecore.service.battle.common.xbean.BattleBean;
import com.kodgames.battlecore.service.battle.common.xbean.PlayerInfo;
import com.kodgames.battlecore.service.battle.common.xbean.Step;
import com.kodgames.battlecore.service.battle.core.MahjongHelper;
import com.kodgames.battleserver.service.battle.core.BattleHelper;
import com.kodgames.battleserver.service.battle.core.creator.CreateContextHelper;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_7DuiHu;
import com.kodgames.battleserver.service.battle.core.hu.HuChecker_PingHu;
import com.kodgames.battleserver.service.battle.core.operation.OperationChecker_Hu;
import java.lang.reflect.Field;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.kodgames.battlecore.service.battle.constant.MahjongConstant_Core.CardType.*;
import static com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType.OPERATE_CAN_HU;
import static com.kodgames.battleserver.service.battle.constant.MahjongConstant.PlayType.OPERATE_CAN_ZI_MO_HU;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OperationChecker_HuTest
{
	private static OperationChecker_Hu checker = new OperationChecker_Hu();
	private static BattleBean context = new BattleBean();
	private static int roleId = 10001;
	private static PlayerInfo playerInfo;
	private static BattleHelper battleHelper;

	@BeforeClass
	public static void beforeClass()
	{
		// 玩家开局手牌数
		context.getCardPool().setPlayCardCount(13);

		playerInfo = new PlayerInfo();
		playerInfo.setRoleId(roleId);

		context.getPlayerIds().add(roleId);
		context.getPlayers().put(roleId, playerInfo);
		context.getCardPool().getSupportedCardTypes().add(WAN.value());
		context.getCardPool().getSupportedCardTypes().add(TIAO.value());
		context.getCardPool().getSupportedCardTypes().add(TONG.value());
		context.getCardPool().getSupportedCardTypes().add(ZI.value());

		battleHelper = new BattleHelper();
		try
		{
			Field field = battleHelper.getClass().getDeclaredField("huChecker");
			field.setAccessible(true);

			JSONObject createContext = CreateContextHelper.createObject(HuChecker.class);

			{
				JSONArray checkers = CreateContextHelper.createArray();
				checkers.element(CreateContextHelper.createObject(HuChecker_PingHu.class));
				checkers.element(CreateContextHelper.createObject(HuChecker_7DuiHu.class));
				createContext.element(HuChecker.KEY_CHECKERS, checkers);
			}

			field.set(battleHelper, HuChecker.create(createContext));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private byte[][] pingHu = {
		{1,1,1,3,3,3,4,4,4,5,5,5,6,6},
		{11,11,11,3,3,3,4,4,4,5,5,5,6,6},
		{11,11,11,23,23,23,4,4,4,5,5,5,6,6},
		{11,11,11,28,27,29,4,4,4,5,5,5,6,6},
		{13,15,14,28,27,29,4,4,4,5,5,5,6,6},
	};

	@Test
	public void pingHuCheck()
	{
		battleHelper.setCurrentInstance();

		for (int i = 0; i < pingHu.length; ++i) {
			assertEquals(14, pingHu[i].length);

			playerInfo.getCards().getHandCards().clear();
			playerInfo.getCards().getHandCards().addAll(MahjongHelper.convert2ByteList(pingHu[i]));

			List<Step> result = checker.doCheck(context, roleId, (byte)0, true);
			assertTrue("index is " + i,result.size() > 0);

			int playType = result.get(0).getPlayType();
			assertTrue("index is " + i, playType == OPERATE_CAN_HU || playType == OPERATE_CAN_ZI_MO_HU);
		}

		battleHelper.resetCurrentInstance();
	}

	@Test
	public void oldVsNew() {
		CheckWin.getInstance().init();
	}
}