<template>
  <div class="content-container">
    <div class="search-form">
      <el-form ref="searchForm" :inline="true" :model="query" label-width="100px"
               class="searchForm">
        <el-form-item label="" prop="q">
          <el-input v-model="query.q" autocomplete="off" :placeholder="$t('searchForm.q')"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm()">{{ $t('search') }}</el-button>
          <el-button @click="resetForm()">{{ $t('reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="filter-items">
      <ul>
        <li :class="activeTag === 'course' ? 'active' : ''" @click="changeTag('course')">
          {{ $t('searchForm.course') }}
        </li>
        <li :class="activeTag === 'exam' ? 'active' : ''" @click="changeTag('exam')">
          {{ $t('searchForm.exam') }}
        </li>
        <li :class="activeTag === 'other' ? 'active' : ''" @click="changeTag('other')">
          {{ $t('searchForm.other') }}
        </li>
      </ul>
    </div>
    <el-row>
      <el-col :span="18">
        <div class="search-res-container">
          <el-row>
            <div class="search-res-total">
              找到相关结果约为 {{ list.length }}个
            </div>
          </el-row>
          <div class="search-res-items">
            <div v-for="item in list" :key="item.key" class="search-res-item" @click="handleClickItem(item)">
              <el-row>
                <el-col :span="2">
                  <div class="item-thumb">
                    <img class="thumb a_scale" :src="item.imageUrl" :alt="item.title"/>
                  </div>
                </el-col>
                <el-col :span="22">
                  <div class="search-res-item-a">
                    <h2>{{ item.title }}</h2>
                    <div class="search-res-item-desc">
                      {{ item.desc }}
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-divider></el-divider>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="search-right-container">
          <div class="search-history">
            <div class="search-history-title">
              <h3>搜索历史</h3>
              <i class="el-icon-delete" @click="deleteHistory" v-if="history.length > 0">全部清除</i>
            </div>
            <div class="search-history-content">
              <div class="history-list" v-if="history.length > 0">
                <span class="history-item" v-for="(item, index) in history" :key="index" @click="clickRank(item)">
                  {{ item }}
                </span>
              </div>
              <p class="search-empty" v-else>你还没搜索过喔～</p>
            </div>
          </div>
          <div class="search-rank">
            <h3>热搜榜</h3>
            <div class="hot-search">
              <el-row class="hot-search-item" v-for="(item, index) in rankWords" :key="index">
                <i class="num">{{index + 1}}</i>
                <a href="javaScript:void(0);" @click="clickRank(item.title)">
                  {{item.title}}
                </a>
              </el-row>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {searchDetailByQuery, searchRank} from '@/api/exam/search'
import {messageSuccess, messageWarn} from '@/utils/util'
import {removeStore, getStore, setStore} from '@/utils/store'
import { SEARCH_HISTORY } from '@/utils/storeMap'
import { v4 as uuidv4 } from 'uuid'

export default {
  data() {
    return {
      list: [],
      activeTag: '',
      query: {
        q: ''
      },
      rankWords: [],
      history: []
    }
  },
  created() {
    if (this.$route.query.query !== '') {
      this.query.q = this.$route.query.query
      this.submitForm(true)
      this.history = getStore({
        name: SEARCH_HISTORY
      })
    }
    this.getSearchRankWords()
  },
  methods: {
    deleteHistory() {
      removeStore({
        name: SEARCH_HISTORY
      })
      this.history = []
    },
    submitForm(isInit = false) {
      if (this.query.q === undefined || this.query.q === '') {
        return
      }
      if (!isInit) {
        const tmp = getStore({
          name: SEARCH_HISTORY
        })
        let res = []
        if (tmp && tmp.length > 0) {
          res = tmp
          let index = tmp.findIndex(item => item === this.query.q)
          if (index === -1) {
            res.push(this.query.q)
          }
        } else {
          res.push(this.query.q)
        }
        setStore({
          name: SEARCH_HISTORY,
          content: res
        })
        this.history = res
      }
      this.getSearchList()
    },
    getSearchList() {
      searchDetailByQuery({q: this.query.q, itemType: this.query.itemType}).then(res => {
        this.list = []
        const {code, result} = res.data
        if (code !== 0) {
          messageWarn(this, this.$t('searchForm.searchFailed'))
          return
        }
        if (result === null) {
          return
        }
        const {items} = result
        const list = []
        if (items) {
          items.forEach(e => {
            e.key = uuidv4()
            list.push(e)
          })
          this.list = list
        }
        messageSuccess(this, this.$t('searchForm.searchSuccess'))
      }).catch(() => {
        messageWarn(this, this.$t('searchForm.searchFailed'))
        this.loading = false
      })
    },
    getSearchRankWords () {
      searchRank().then(res => {
        const {code, result} = res.data
        if (code === 0 && result) {
          this.rankWords = result.items
        }
      })
    },
    resetForm() {
      this.query.q = ''
      this.query.itemType = ''
      this.getSearchList()
    },
    changeTag(tag) {
      this.activeTag = tag
      this.query.itemType = tag
      this.submitForm()
    },
    handleClickItem(item) {
      const {id, type} = item
      if (type === 'course') {
        this.$router.push({name: 'course-details', query: {courseId: id}})
      } else if (type === 'exam') {
        this.$router.push({name: 'exam-details', query: {examId: id}})
      } else {
        // TODO
      }
    },
    clickRank(rankWord) {
      this.query.q = rankWord
      this.submitForm()
    }
  }
}
</script>

<style lang="scss" rel="stylesheet/scss" scoped>
.filter-items {
  margin: 0 auto 12px;
  padding: 0 12px;
  width: 98%;
  box-shadow: 0 5px 15px 0 rgba(82, 94, 102, .1);
  border-radius: 4px;

  ul {
    margin: 0;
    overflow: hidden;
  }

  .active {
    color: #fff;
    background: #409eff;
  }

  li {
    list-style: none;
    display: block;
    float: left;
    margin: 10px;
    padding: 0 15px;
    height: 24px;
    line-height: 24px;
    color: #666;
    font-size: 13px;
    border-radius: 5px;
    cursor: pointer;
  }
}

.search-res-container {
  background-color: #fff;
  margin: 0 auto 30px;
  padding: 15px 10px;
  box-shadow: 0 5px 15px 0 rgba(82, 94, 102, .1);
  border-radius: 4px;
}

.search-res-total {
  float: left;
  font-size: 14px;
  color: #8C92A4;
  height: 20px;
  line-height: 20px;
  margin-top: 8px;
}

.search-res-item-a {
  margin-left: 20px;
  display: block;
  min-height: 0.7rem;
  color: #323232;
}

.search-res-item-desc {
  font-size: 13px;
  font-weight: 400;
  margin-top: 10px;
  overflow: hidden;
  color: #8a8a8a;
  max-width: 54em;
}

.search-res-item {
  margin-top: 20px;
  cursor: pointer;
}

.search-res-items .item-thumb {
  overflow: hidden;
  -ms-transform: rotate(0deg);
  transform: rotate(0deg);
}

.thumb {
  width: 100px;
  border-radius: 8px;
  background-repeat: no-repeat;
  -webkit-background-size: cover;
  background-size: cover;
}

.search-right-container {
  margin-left: 30px;
}

.search-history {
  padding: 16px;
  background-color: #fff;
  box-shadow: 0 5px 15px 0 rgba(82, 94, 102, .1);
  border-radius: 4px;
}

.search-history-title {
  display: flex;
  justify-content: space-between;
}

.search-rank {
  padding: 16px;
  background-color: #fff;
  box-shadow: 0 5px 15px 0 rgba(82, 94, 102, .1);
  border-radius: 4px;
  margin-top: 12px;
}

.hot-search-item {
  padding-left: 6px;
  margin: 0 0 16px;
  position: relative;
}

.hot-search-item a {
  display: block;
  white-space: nowrap;
  overflow: hidden;
  font-size: 12px;
  color: #323232;
  margin-left: 12px;
  font-weight: normal;
}

.hot-search-item:nth-child(1) {
  margin-top: 16px;
}

.hot-search-item:nth-child(1) .num {
  color: #fe2d46;
}

.hot-search-item .num {
  display: block;
  position: absolute;
  left: 0;
  top: 0;
  width: 0.25rem;
  font-size: .16rem;
  color: #faa90e;
  text-align: left;
  font-weight: bold;
}

.el-icon-delete {
  font-weight: normal;
  color: #8a8a8a;
  cursor: pointer;
  font-size: 13px;
}

.search-empty {
  font-size: .14rem;
  color: #b5b5b5;
  text-align: center;
}
.search-history-content {
  padding-top: 10px;
  .history-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    .history-item {
      padding: 2px 12px;
      font-size: 12px;
      color: #3e454d;
      cursor: pointer;
      background: #f2f5f7;
      border-radius: 16px;

      &:hover {
        color: #2080f7;
      }
    }
  }
}

</style>
