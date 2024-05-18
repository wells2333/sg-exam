import {unref} from "vue";

export function filterMenuIds(data, menuIds) {
  if (data.value.length > 0) {
    data.value.forEach(e => {
      handleFilterMenuIds(unref(data), unref(e), menuIds);
    });
  }
}

export function handleFilterMenuIds(data, e, menuIds) {
  let includeAllChildren = undefined;
  if (e.children && e.children.length > 0) {
    includeAllChildren = isIncludeAll(menuIds, e.children);
    e.children.forEach(ee => {
      handleFilterMenuIds(unref(e), unref(ee), menuIds);
    });
  }
  // 删除这个ID
  if (includeAllChildren !== undefined && !includeAllChildren && menuIds.includes(e.id)) {
    menuIds.splice(menuIds.findIndex(item => item === e.id), 1);
  }
}

// 判断是否全部包含
export function isIncludeAll(menuIds, data) {
  let include = true;
  if (data) {
    data.forEach(e => {
      if (e.children !== undefined && e.children.length > 0) {
        include = false;
      }
      if (!menuIds.includes(e.id)) {
        include = false;
      }
    });
  }
  return include;
}
