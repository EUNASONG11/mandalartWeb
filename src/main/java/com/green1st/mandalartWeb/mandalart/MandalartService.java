package com.green1st.mandalartWeb.mandalart;

import com.green1st.mandalartWeb.mandalart.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MandalartService {
    private final MandalartMapper mapper;

    // 완료 했다가 다시 취소하면 색상이 변경되도록 설정
    public List<MandalartGetRes> getMandalart(MandalartGetReq p) {
        List<MandalartGetRes> resList = mapper.getMandalart(p);

        ColorCodes colorCodes = new ColorCodes();

        for(MandalartGetRes item : resList) {
            int index = getCompleted(item, resList);
            switch (item.getDepth()) {
                case 0:
                    if(index > 0) {
                        item.setBgColor(colorCodes.getTitleColor().get(index - 1));
                    }
                    break;
                case 1:
                    if(index > 0) {
                        item.setBgColor(colorCodes.getSubTitleColor().get(index - 1));
                    }
                    break;
                case 2:
                    if(item.getCompletedFg() == 1) {
                        item.setBgColor(colorCodes.getDefaultColor().get(0));
                    }
                    break;
            }
        }

        return resList;
    }
    // 특정 ID의 만다라트 데이터를 가져오는 메서드
    List<GraphRes> graphMandalart (GraphReq p) {

        return mapper.graphMandalart(p);
    }

    List<MandalartGetImminentRes> getImProject (MandalartGetImminentReq p){
//        if (p.getUserId() <= 0) {
//            throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
//        }
        List<MandalartGetImminentRes> imminentResList = mapper.getImProject(p);

        return imminentResList;
    }

    public int patchMandalart(MandalartPostReq p) {
        if (p.getStartDate() != null && p.getFinishDate() != null && !p.getStartDate().isBefore(p.getFinishDate())) {
            throw new IllegalArgumentException("시작일은 종료일보다 이전이어야 합니다.");
        }

        // DB 업데이트 실행
        int updatedRows = mapper.patchMandalart(p);

        // 업데이트된 튜플 수 반환
        return updatedRows;
    }

    public int getCompleted(MandalartGetRes res,  List<MandalartGetRes> list) {
        int completedCnt = 0;
        for(MandalartGetRes item : list) {
            if(res.getMandalartId() == item.getParentId() && item.getCompletedFg() == 1) {
                completedCnt++;
            }
        }
        return completedCnt;
    }
}
