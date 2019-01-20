package org.luban.common.serialize.msgpack;

import java.util.List;

/**
 * User: shijingui
 * Date: 2016/3/10
 */
public class VariousTypesDemo {

    public static void main() {
        ResultDto resultDto = new ResultDto();
        PageFacade pageFacade = new PageFacade();


    }


    static class PageFacade {
        private List<?> data;

        public void setData(List<?> data) {
            this.data = data;
        }

        public List<?> getData() {
            return this.data;
        }
    }

    static class ResultDto {
        private Object resultDto;

        public Object getResultDto() {
            return resultDto;
        }

        public void setResultDto(Object resultDto) {
            this.resultDto = resultDto;
        }
    }

}
