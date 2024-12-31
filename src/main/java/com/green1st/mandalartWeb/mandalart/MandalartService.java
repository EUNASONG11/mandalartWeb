package com.green1st.mandalartWeb.mandalart;

import com.green1st.mandalartWeb.mandalart.model.*;
import com.green1st.mandalartWeb.project.ProjectMapper;
import com.green1st.mandalartWeb.project.model.ProjectDetailDto;
import com.green1st.mandalartWeb.shared_project.SharedProjectMapper;
import com.green1st.mandalartWeb.shared_project.model.SharedProjectDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MandalartService {
    private final MandalartMapper mapper;
    private final ProjectMapper projectMapper;
    private final SharedProjectMapper sharedProjectMapper;

    // 완료 했다가 다시 취소하면 색상이 변경되도록 설정
    public MandalartGetResVer2 getMandalart(MandalartGetReq p) {
        MandalartGetResVer2 mandalartGetResVer2 = new MandalartGetResVer2();
        ProjectDetailDto projectDetailDto = new ProjectDetailDto();

        // 프로젝트 정보 조회
        projectDetailDto = projectMapper.selProjectDetail(p.getProjectId());

        mandalartGetResVer2.setProjectId(projectDetailDto.getProjectId());
        mandalartGetResVer2.setTitle(projectDetailDto.getTitle());
        mandalartGetResVer2.setContent(projectDetailDto.getContent());
        mandalartGetResVer2.setNickName(projectDetailDto.getNickName());
        mandalartGetResVer2.setSharedFg(projectDetailDto.getSharedFg());
        mandalartGetResVer2.setCreatedAt(projectDetailDto.getCreatedAt());

        List<MandalartGetRes> resList = mapper.getMandalart(p);

        ColorCodes colorCodes = new ColorCodes();

        for(MandalartGetRes item : resList) {
            int index = getCompleted(item, resList);
            int total = 8;
            switch (item.getDepth()) {
                case 0:
                    if(index > 0) {
                        item.setBgColor(colorCodes.getTitleColor().get(index - 1));
                    }
                    break;
                case 1:
                    if(index > 0) {
                        double percentage = (double) index / total * 100;
                        item.setCompletedPer(percentage);
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

        mandalartGetResVer2.setMandalart(resList);

        return mandalartGetResVer2;
    }

    // 완료 했다가 다시 취소하면 색상이 변경되도록 설정
    public MandalartSharedGetResVer2 getSharedMandalart(MandalartSharedGetReq p) {
        MandalartSharedGetResVer2 mandalartSharedGetResVer2 = new MandalartSharedGetResVer2();
        SharedProjectDetailDto sharedProjectDetailDto;

        log.info("asdsadsadadsa{}", p.getProjectId());

        // 프로젝트 정보 조회
        sharedProjectDetailDto = sharedProjectMapper.selSharedProjectDetail(p);

        mandalartSharedGetResVer2.setProjectId(sharedProjectDetailDto.getProjectId());
        mandalartSharedGetResVer2.setTitle(sharedProjectDetailDto.getTitle());
        mandalartSharedGetResVer2.setContent(sharedProjectDetailDto.getContent());
        mandalartSharedGetResVer2.setNickName(sharedProjectDetailDto.getNickName());
        mandalartSharedGetResVer2.setLikeFg(sharedProjectDetailDto.getLikeFg());
        mandalartSharedGetResVer2.setCreatedAt(sharedProjectDetailDto.getCreatedAt());

        MandalartGetReq mandalartGetReq = new MandalartGetReq();
        mandalartGetReq.setProjectId(p.getProjectId());

        List<MandalartGetRes> resList = mapper.getMandalart(mandalartGetReq);

        ColorCodes colorCodes = new ColorCodes();

        for(MandalartGetRes item : resList) {
            int index = getCompleted(item, resList);
            int total = 8;
            switch (item.getDepth()) {
                case 0:
                    if(index > 0) {
                        item.setBgColor(colorCodes.getTitleColor().get(index - 1));
                    }
                    break;
                case 1:
                    if(index > 0) {
                        double percentage = (double) index / total * 100;
                        item.setCompletedPer(percentage);
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

        mandalartSharedGetResVer2.setMandalart(resList);

        return mandalartSharedGetResVer2;
    }

    List<MandalartGetImminentRes> getImProject (MandalartGetImminentReq p){
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
