package entity;

import java.util.ArrayList;

/**
 * Created by lyd10892 on 2016/8/23.
 */

public class HotelEntity {

    public ArrayList<TagsEntity> allTagsList;

    public class TagsEntity {
        public String tagsName;
        public ArrayList<TagInfo> tagInfoList;

        public class TagInfo {
            public String tagName;
        }
    }

}
