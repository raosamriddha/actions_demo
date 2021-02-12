package org.lattice.spectrum_backend_final.dao.manager;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ReferenceCalculator;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import java.sql.Ref;

public class ReferenceCalculatorManager {

    private ReferenceCalculator referenceCalculator = new ReferenceCalculator();

    public JSONObject getCalculatedResult(JSONObject parameters){
        String modeName = parameters.getString(ApiConstant.MODE_NAME);
        double feedWt = parameters.getDouble(ApiConstant.FEED_START_WEIGHT);
        double permHoldUp = parameters.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP);
        double concentrationFact1 = parameters.getDouble(ApiConstant.CONCENTRATION_FACT + ApiConstant.UNDER_SCORE + 1);
        double concentrationFact2 = parameters.getDouble(ApiConstant.CONCENTRATION_FACT + ApiConstant.UNDER_SCORE + 2);
        double diaFiltrationVol1 = parameters.getDouble(ApiConstant.DIAFILTRATION_VOL_1);
        double diaFiltrationVol2 = parameters.getDouble(ApiConstant.DIAFILTRATION_VOL_2);


        switch (modeName){
            case ApiConstant.C_MODE:
                referenceCalculator.setFeedWtAfterC1(
                         feedWt / concentrationFact1
                );
                referenceCalculator.setPermeateWtAfterC1(
                        feedWt - ( referenceCalculator.getFeedWtAfterC1() + permHoldUp )
                );
                break;
            case ApiConstant.CFC_MODE:
                referenceCalculator.setFeedWtAfterCFC(feedWt);
                referenceCalculator.setPermeateWtAfterCFC(
                        feedWt * (concentrationFact1 - 1)
                                - permHoldUp
                );
                referenceCalculator.setFeedBuffer1(
                        feedWt * (concentrationFact1 - 1)
                );
                break;
            case ApiConstant.D_MODE:
                referenceCalculator.setFeedWtAfterD1(feedWt);
                referenceCalculator.setPermeateWtAfterD1(
                        (feedWt * diaFiltrationVol1) - permHoldUp
                );
                referenceCalculator.setFeedBuffer1(
                        feedWt * diaFiltrationVol1
                );
                break;
            case ApiConstant.CD_MODE:
                referenceCalculator.setFeedWtAfterC1(
                        feedWt / concentrationFact1
                );
                referenceCalculator.setPermeateWtAfterC1(
                        feedWt - ( referenceCalculator.getFeedWtAfterC1() + permHoldUp )
                );
                referenceCalculator.setFeedWtAfterD1(referenceCalculator.getFeedWtAfterC1());
                referenceCalculator.setPermeateWtAfterD1(
                        (referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1)
                                + referenceCalculator.getPermeateWtAfterC1()
                );
                referenceCalculator.setFeedBuffer1(
                        referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1
                );
                break;
            case ApiConstant.CDC_MODE:
                referenceCalculator.setFeedWtAfterC1(
                        feedWt / concentrationFact1
                );
                referenceCalculator.setPermeateWtAfterC1(
                        feedWt - (referenceCalculator.getFeedWtAfterC1() + permHoldUp)
                );
                referenceCalculator.setFeedWtAfterD1(referenceCalculator.getFeedWtAfterC1());
                referenceCalculator.setPermeateWtAfterD1(
                        (referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1)
                                + referenceCalculator.getPermeateWtAfterC1()
                );
                referenceCalculator.setFeedWtAfterC2(
                        feedWt / concentrationFact2
                );
                referenceCalculator.setPermeateWtAfterC2(
                        (referenceCalculator.getFeedWtAfterD1() - referenceCalculator.getFeedWtAfterC2())
                                + referenceCalculator.getPermeateWtAfterD1()
                );
                referenceCalculator.setFeedBuffer1(
                        referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1
                );
                break;
            case ApiConstant.CFDC_MODE:
                referenceCalculator.setFeedWtAfterCFC(feedWt);
                referenceCalculator.setPermeateWtAfterCFC(
                        feedWt * (concentrationFact1 - 1)
                                - permHoldUp
                );
                referenceCalculator.setFeedWtAfterD1(feedWt);
                referenceCalculator.setPermeateWtAfterD1(
                        (feedWt * diaFiltrationVol1)
                                + referenceCalculator.getPermeateWtAfterCFC()
                );
                referenceCalculator.setFeedWtAfterC1(
                        feedWt / concentrationFact2
                );
                referenceCalculator.setPermeateWtAfterC1(
                        (referenceCalculator.getFeedWtAfterD1() - referenceCalculator.getFeedWtAfterC1())
                                + referenceCalculator.getPermeateWtAfterD1()
                );
                referenceCalculator.setFeedBuffer1(
                        feedWt * (concentrationFact1 - 1)
                );
                referenceCalculator.setFeedBuffer2(
                        feedWt * diaFiltrationVol1
                );

                break;
            case ApiConstant.CDCD_MODE:
                referenceCalculator.setFeedWtAfterC1(
                        feedWt / concentrationFact1
                );
                referenceCalculator.setPermeateWtAfterC1(
                        feedWt - (referenceCalculator.getFeedWtAfterC1() + permHoldUp)
                );
                referenceCalculator.setFeedWtAfterD1(referenceCalculator.getFeedWtAfterC1());
                referenceCalculator.setPermeateWtAfterD1(
                        (referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1)
                                + referenceCalculator.getPermeateWtAfterC1()
                );
                referenceCalculator.setFeedWtAfterC2(
                        feedWt / concentrationFact2
                );
                referenceCalculator.setPermeateWtAfterC2(
                        (referenceCalculator.getFeedWtAfterD1() - referenceCalculator.getFeedWtAfterC2())
                                + referenceCalculator.getPermeateWtAfterD1()
                );
                referenceCalculator.setFeedWtAfterD2( referenceCalculator.getFeedWtAfterC2() );
                referenceCalculator.setPermeateWtAfterD2(
                        ( referenceCalculator.getFeedWtAfterD2() * diaFiltrationVol2 )
                                + referenceCalculator.getPermeateWtAfterC2()
                );
                referenceCalculator.setFeedBuffer1(
                        referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1
                );
                referenceCalculator.setFeedBuffer2(
                        referenceCalculator.getFeedWtAfterC2() * diaFiltrationVol2
                );

                break;
            case ApiConstant.CDDC_MODE:
                referenceCalculator.setFeedWtAfterC1(
                        feedWt / concentrationFact1
                );
                referenceCalculator.setPermeateWtAfterC1(
                        feedWt - (referenceCalculator.getFeedWtAfterC1() + permHoldUp)
                );
                referenceCalculator.setFeedWtAfterD1(referenceCalculator.getFeedWtAfterC1());
                referenceCalculator.setPermeateWtAfterD1(
                        ( referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1 )
                                + referenceCalculator.getPermeateWtAfterC1()
                ) ;
                referenceCalculator.setFeedWtAfterD2(referenceCalculator.getFeedWtAfterD1());
                referenceCalculator.setPermeateWtAfterD2(
                        (referenceCalculator.getFeedWtAfterD2() * diaFiltrationVol2) + referenceCalculator.getPermeateWtAfterD1()
                );
                referenceCalculator.setFeedWtAfterC2(
                        feedWt / concentrationFact2
                );
                referenceCalculator.setPermeateWtAfterC2(
                        (referenceCalculator.getFeedWtAfterD2() - referenceCalculator.getFeedWtAfterC2())
                                + referenceCalculator.getPermeateWtAfterD2()
                );
                referenceCalculator.setFeedBuffer1(
                        referenceCalculator.getFeedWtAfterC1() * diaFiltrationVol1
                );
                referenceCalculator.setFeedBuffer2(
                        referenceCalculator.getFeedWtAfterC2() * diaFiltrationVol2
                );

                break;
        }

        return new JSONObject(referenceCalculator);
    }

}
