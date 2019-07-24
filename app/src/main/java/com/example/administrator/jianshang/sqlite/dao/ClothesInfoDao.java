package com.example.administrator.jianshang.sqlite.dao;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;

import com.example.administrator.jianshang.R;
import com.example.administrator.jianshang.bean.DBDaHuoInfoBean;
import com.example.administrator.jianshang.bean.DBDahuoImgBean;
import com.example.administrator.jianshang.bean.DBFuliaoInfoBean;
import com.example.administrator.jianshang.bean.ClothesInfoBean;
import com.example.administrator.jianshang.sqlite.MyOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class ClothesInfoDao {
    private MyOpenHelper myOpenHelper = null;
    private SQLiteDatabase db = null;
    private Context context;

    public ClothesInfoDao(Context context) {
        this.context = context;
        myOpenHelper = new MyOpenHelper(context, "jianshang.db", null, 3);
    }

    /**
     * 添加大货款式
     *
     * @param clothesInfoBean
     * @return
     */
    public boolean addNewClothes(ClothesInfoBean clothesInfoBean) {
        boolean resultBoolean = false;
        db = myOpenHelper.getWritableDatabase();
        TbDahuoInfoDao tbDahuoInfoDao = new TbDahuoInfoDao(context);
        TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(context);
        TbFuliaoInfoDao tbFuliaoInfoDao = new TbFuliaoInfoDao(context);

        DBDaHuoInfoBean dbDaHuoInfoBean;
        ArrayList<DBDahuoImgBean> dbDahuoImgBeans;      //该款式的款式图片集合
        ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;  //该款式的面辅料信息集合

        dbDaHuoInfoBean = clothesInfoBean.getDbDaHuoInfoBean();
        dbDahuoImgBeans = clothesInfoBean.getDbDahuoImgBeans();
        dbFuliaoInfoBeans = clothesInfoBean.getDbFuliaoInfoBeans();

        boolean addDahuoInfoIsSuccess = false;
        boolean addAllDahuoImgIsSuccess = false;
        boolean addAllFuliaoInfoSuccess = false;


//        int lastId = tbDahuoInfoDao.getIdWithLast(db);
//        int dahuoId = lastId + 1;


        try {
            db.beginTransaction(); // 开启事务

            // 添加大货信息
            addDahuoInfoIsSuccess = tbDahuoInfoDao.addDahuoInfo(dbDaHuoInfoBean, db);

            int dahuoId = tbDahuoInfoDao.getIdWithLast(db);
            //int dahuoId = lastId + 1;

            int lid = tbDahuoInfoDao.getLastID(db);

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
     * 根据款式信息id查询
     * @param clothesInfoID
     * @return
     */
  public ClothesInfoBean getClothesInfoBeanForClothesInfoID(int clothesInfoID){
      ClothesInfoBean clothesInfoBean = new ClothesInfoBean();
      TbDahuoInfoDao tbDahuoInfoDao = new TbDahuoInfoDao(context);
      TbDahuoImgDao tbDahuoImgDao = new TbDahuoImgDao(context);
      TbFuliaoInfoDao tbFuliaoInfoDao = new TbFuliaoInfoDao(context);

      DBDaHuoInfoBean dbDaHuoInfoBean;
      ArrayList<DBDahuoImgBean> dbDahuoImgBeans;
      ArrayList<DBFuliaoInfoBean> dbFuliaoInfoBeans;

      dbDaHuoInfoBean =  tbDahuoInfoDao.getDBDaHuoInfoBeanForClothesID(clothesInfoID);
      dbDahuoImgBeans = tbDahuoImgDao.getDBDaHuoImgBeansForClothesID(clothesInfoID);
      dbFuliaoInfoBeans = tbFuliaoInfoDao.getDBDaHuoFuliaoInfoBeansForClothesID(clothesInfoID);

      if (dbDaHuoInfoBean != null){
          clothesInfoBean.setDbDaHuoInfoBean(dbDaHuoInfoBean);

          if (dbDahuoImgBeans != null){
              clothesInfoBean.setDbDahuoImgBeans(dbDahuoImgBeans);
          }
          if (dbFuliaoInfoBeans != null){
              clothesInfoBean.setDbFuliaoInfoBeans(dbFuliaoInfoBeans);
          }
      }

      return clothesInfoBean;
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
     * @param dbDaHuoInfoBean
     * @return
     */
    public boolean removeClothesForId(DBDaHuoInfoBean dbDaHuoInfoBean) {
        int id = dbDaHuoInfoBean.getId();
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
            removeDahuoInfoIsSuccess = tbDahuoInfoDao.removeDahuoInfoForDahuoId(id, db);//删除款式信息数据
            ArrayList<String> dhimgs = tbDahuoImgDao.getImgsWithDahuoIdHaveDb(id, db);
            ArrayList<String> flimgs = tbFuliaoInfoDao.getImgsWithDahuoIdHaveDb(id, db);

            if (haveImg) {
                removeDahuoImgIsSuccess = tbDahuoImgDao.removeDahuoImgForDahuoId(id, db);//删除款式大货图片数据
            } else {
                removeDahuoImgIsSuccess = true;
            }
            if (haveFuliao) {
                removeFuliaoInfoSuccess = tbFuliaoInfoDao.removeFuliaoInfoForDahuoId(id, db);
            } else {
                removeFuliaoInfoSuccess = true;
            }


            if (removeDahuoInfoIsSuccess && removeDahuoImgIsSuccess && removeFuliaoInfoSuccess) {
                //删除本地图片
                String folderName = context.getString(R.string.my_photo_folder_name);
                File file_img_kscb = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/" + folderName + "/" + dbDaHuoInfoBean.getChengbenImg());
                File file_img_ksfm = new File(Environment.getExternalStorageDirectory().getPath() +
                        "/" + folderName + "/" + dbDaHuoInfoBean.getFengmianImg());

                if (file_img_kscb.exists()) {
                    file_img_kscb.delete();//删除款式成本图片
                }
                if (file_img_ksfm.exists()) {
                    file_img_ksfm.delete();//删除款式封面图片
                }

                //删除大货款式图片
                if (haveImg) {
                    for (String img :
                            dhimgs) {
                        File file_img_kstp = new File(Environment.getExternalStorageDirectory().getPath() +
                                "/" + folderName + "/" + img);

                        if (file_img_kstp.exists()) {
                            file_img_kstp.delete();
                        }
                        //file_img_kstp.delete();
                    }
                }

                //删除辅料图片
                if (haveFuliao) {

                    for (String img :
                            flimgs) {
                        File file_img_fltp = new File(Environment.getExternalStorageDirectory().getPath() +
                                "/" + folderName + "/" + img);
                        if (file_img_fltp.exists()) {
                            file_img_fltp.delete();
                        }
                       // file_img_fltp.delete();
                    }
                }



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
