import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {AttachmentGroupApi} from "/@/api/api";

export const getAttachGroupList = (params?: object) =>
  defHttp.get<ApiRes>({url: AttachmentGroupApi.GroupList, params});

export const createAttachGroup = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: AttachmentGroupApi.Base,
      params,
    }
  );
};

export const updateAttachGroup = (id: string, params?: object
) => {
  return defHttp.put<ApiRes>(
    {
      url: AttachmentGroupApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteAttachGroup = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: AttachmentGroupApi.Base + '/' + id,
    }
  );
};


