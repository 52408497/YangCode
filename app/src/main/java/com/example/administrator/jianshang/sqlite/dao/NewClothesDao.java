package com.example.administrator.jianshang.sqlite.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.NewDaHuoClothesBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.util.ArrayList;

public class NewClothesDao {
    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
    private Context context;

    public NewClothesDao(Context context) {
        this.context = context;
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }

    /**
     * 添加大货款式
     *
     * @param newDaHuoClothesBean
     * @return
     */
    public boolean addNewClothes(NewDaHuoClothesBean newDaHuoClothesBean) {
        boolean resultBoolean = false;
        db = myOpenHelper.getWritableDatabase();
        TbDahuoInfoDao tbDahuoInfoDao = new TbDahuoInfoDao(context);
        TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(context);
        TbFuliaoInfoDao tbFuliaoInfoDao = new TbFuliaoInfoDao(context);

        DBDaHuoInfoBean dbDaHuoInfoBean;
        ArrayList<DBDahuoImgBean> dbDahuoImgBeans;      //该款式的款式图片集合
        ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;  //该款式的面辅料信息集合

        dbDaHuoInfoBean = newDaHuoClothesBean.getDbDaHuoInfoBean();
        dbDahuoImgBeans = newDaHuoClothesBean.getDbDahuoImgBeans();
        dbFuliaoInfoBeans = newDaHuoClothesBean.getDbFuliaoInfoBeans();

        boolean addDahuoInfoIsSuccess = false;
        boolean addAllDahuoImgIsSuccess = false;
        boolean addAllFuliaoInfoSuccess = false;


        int lastId = tbDahuoInfoDao.getIdWithLast(db);
        int dahuoId = lastId + 1;


        try {
            db.beginTransaction(); // 开启事务

            // 添加大货信息
            addDahuoInfoIsSuccess = tbDahuoInfoDao.addDahuoInfo(dbDaHuoInfoBean, db);


            //添加大货图片信息
            if (dbDahuoImgBeans.size() > 0) {
                for (DBDahuoImgBean bean :
                        dbDahuoImgBeans) {
                    addAllDahuoImgIsSuccess = tbDahuoImgDao.addDahuoImg(dahuoId, bean, db);
                    if (!addAllDahuoImgIsSuccess) {
                        break;
                    }
                }
            } else {
                addAllDahuoImgIsSuccess = true;
            }

            //添加辅料信息
            if (dbFuliaoInfoBeans.size() > 0) {
                for (DBFuliaoInfoBean bean :
                        dbFuliaoInfoBeans) {
                    addAllFuliaoInfoSuccess = tbFuliaoInfoDao.addFuliaoInfo(dahuoId, bean, db);
                    if (!addAllFuliaoInfoSuccess) {
                        break;
                    }
                }
            } else {
                addAllFuliaoInfoSuccess = true;
            }

            if (addDahuoInfoIsSuccess && addAllDahuoImgIsSuccess && addAllFuliaoInfoSuccess) {
                db.setTransactionSuccessful(); // 事务默认是失败的，要设置成功，否则数据不会修改
                resultBoolean = true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            db.endTransaction(); // 结束事务
            db.close();
            myOpenHelper.close();
        }
        return resultBoolean;
    }

    /**
     * 根据年份信息及款号查询该款式是否存在
     *
     * @param timeData
     * @param sKH
     * @return
     */
    public boolean getInfoForYearAndKH(String timeData, String sKH) {
        TbDahuoInfoDao dao = new TbDahuoInfoDao(context);
        return dao.getInfoForYearAndKH(timeData, sKH);
    }

    /**
     * 根据id删除款式
     *
     * @param id
     * @return
     */
    public boolean removeClothesForId(int id) {
        boolean success = false;

        db = myOpenHelper.getWritableDatabase();
        TbDahuoInfoDao tbDahuoInfoDao = new TbDahuoInfoDao(context);
        TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(context);
        TbFuliaoInfoDao tbFuliaoInfoDao = new TbFuliaoInfoDao(context);

        boolean haveImg = false;
        boolean haveFuliao = false;
        boolean removeDahuoInfoIsSuccess = false;
        boolean removeDahuoImgIsSuccess = false;
        boolean removeFuliaoInfoSuccess = false;

        //先要根据款式id查询该款式是否有款式图片和辅料
        haveImg = tbDahuoImgDao.isHaveImgWithDahuoId(id);
        haveFuliao = tbFuliaoInfoDao.isHaveFuliaoInfoWithDahuoId(id);


        try {
            db.beginTransaction(); // 开启事务
            removeDahuoInfoIsSuccess = tbDahuoInfoDao.removeDahuoInfoForDahuoId(id, db);

            if (haveImg) {
                removeDahuoImgIsSuccess = tbDahuoImgDao.removeDahuoImgForDahuoId(id, db);
            } else {
                removeDahuoImgIsSuccess = true;
            }
            if (haveFuliao) {
                removeFuliaoInfoSuccess = tbFuliaoInfoDao.removeFuliaoInfoForDahuoId(id, db);
            } else {
                removeFuliaoInfoSuccess = true;
            }


            if (removeDahuoInfoIsSuccess && removeDahuoImgIsSuccess && removeFuliaoInfoSuccess) {
                db.setTransactionSuccessful(); // 事务默认是失败的，要设置成功，否则数据不会修改
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            db.endTransaction(); // 结束事务
            db.close();
            myOpenHelper.close();
        }


        return success;
    }
}
