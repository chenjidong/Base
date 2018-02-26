package com.kuky.base.model

import com.kuky.base.contract.CeilingContract
import com.kuky.base.entity.CeilingData
import com.kuky.baselib.ResultCallBack

/**
 * @author Kuky
 */
class CeilingModel : CeilingContract.ICeilingModel {

    override fun getCeilings(callBack: ResultCallBack.OnListResultBack<MutableList<CeilingData>>) {
        val ceilingList: MutableList<CeilingData>? = mutableListOf()

        (0 until 20).map {
            val ceil = CeilingData("分组类别01", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组01__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        (0 until 20).map {
            val ceil = CeilingData("分组类别02", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组02__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        (0 until 20).map {
            val ceil = CeilingData("分组类别03", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组03__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        if (ceilingList != null && !ceilingList.isEmpty()) callBack.onSucceed(ceilingList)
        else if (ceilingList != null && ceilingList.isEmpty()) callBack.onEmpty()
        else callBack.onFailed()
    }


    override fun getMoreCeilings(size: Int, callBack: ResultCallBack.OnHttpResultBack<MutableList<CeilingData>>) {
        val ceilingList: MutableList<CeilingData>? = mutableListOf()

        (0 until 20).map {
            val ceil = CeilingData("分组类别04", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组04__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        (0 until 20).map {
            val ceil = CeilingData("分组类别05", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组05__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        (0 until 20).map {
            val ceil = CeilingData("分组类别06", "https://github.com/kukyxs/CuteAvatar/blob/master/01/01_1.jpg?raw=true", "分组06__子项${it + 1}")
            ceilingList!!.add(ceil)
        }

        if (size >= 120) callBack.onFailed("全部加载完毕")
        else if (ceilingList != null && !ceilingList.isEmpty()) callBack.onSucceed(ceilingList)
    }
}
