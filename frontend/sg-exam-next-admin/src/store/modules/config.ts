import {defineStore} from "pinia";
import {getAuthCache, setAuthCache} from "/@/utils/auth";
import {SYS_CONFIG_KEY} from "/@/enums/cacheEnum";
import {SysConfig} from "/#/store";

interface SysConfigState {
  sysConfig: Nullable<SysConfig>
}

export const useSysConfigStore = defineStore({
  id: 'sys-config',
  state: (): SysConfigState => ({
    sysConfig: null
  }),
  getters: {
    getSysConfig(): SysConfig {
      return this || getAuthCache<SysConfig>(SYS_CONFIG_KEY) ||{};
    }
  },
  actions: {
    setSysConfig(info: SysConfig | undefined) {
      this.sysConfig = info;
      setAuthCache(SYS_CONFIG_KEY, info);
    },
  }
});
