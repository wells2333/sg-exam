import {defHttp} from '/@/utils/http/axios';
import {ApiRes} from "/@/api/constant";
import {MessageApi, MessageReceiverApi, MessageReadApi} from "/@/api/api";

export const getMessageList = (params?: object) =>
  defHttp.get<ApiRes>({url: MessageApi.MessageList, params});

export const getMessageInfo = (id: string, params?: object) =>
  defHttp.get<ApiRes>({url: MessageApi.Base + '/' + id, params});

export const selectUsers = (params?: object) =>
  defHttp.get<ApiRes>({url: MessageApi.Base + '/selectUsers', params});

export const createMessage = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: MessageApi.Base,
      params,
    }
  );
};

export const updateMessage = (id: string, params?: object) => {
  return defHttp.put<ApiRes>(
    {
      url: MessageApi.Base + '/' + id,
      params,
    }
  );
};

export const deleteMessage = (id: string) => {
  return defHttp.delete<ApiRes>(
    {
      url: MessageApi.Base + '/' + id,
    }
  );
};

export const getMessageListByReceiver = (params?: object) =>
  defHttp.get<ApiRes>({url: MessageReceiverApi.MessageList, params});

export const getUserMessageList = (params?: object) =>
  defHttp.get<ApiRes>({url: MessageApi.Base + '/userMessageList', params});

export const readMessage = (params?: object
) => {
  return defHttp.post<ApiRes>(
    {
      url: MessageReadApi.Base + '/readMessage',
      params,
    }
  );
};
