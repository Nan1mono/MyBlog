package com.ljc.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljc.blog.dao.mapper.TagMapper;
import com.ljc.blog.dao.pojo.Tag;
import com.ljc.blog.service.TagService;
import com.ljc.blog.vo.Result;
import com.ljc.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    /**
     * 根据文章Id查询文章的类别
     * 注意：文章类别归档时，可以有多个类别，所以我们用List
     * 同样的，我们依然采用vo层的数据转移将数据转换为方便查看的类型
     */
    @Override
    public List<TagVo> listTagsByArticleId(Long articleId) {
        List<Tag> tagList = tagMapper.listTagsByArticleId(articleId);
        List<TagVo> tagVoList = copyList(tagList);
        return tagVoList;
    }

    @Override
    public Result hots(int limit) {
        // 查询热门标签的id
        List<Long> tagIds = tagMapper.listHotTagIds(limit);
        // 如果没有文章 那查询标签名时会触发空指针异常 即为null，为了防止这种情况的发生，如果tagIds为空，传一个进查询结果直接返回，不再继续查询标签名
        if (CollectionUtils.isEmpty(tagIds)){
            return Result.success(Collections.emptyList());
        }
        // 根据热门标签id查询热门标签的名字
        // select * from tag where id (1,2,3);
        List<Tag> tagList = tagMapper.listTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result listAllTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagVo> tagVoList = copyList(tagList);
        return Result.success(tagVoList);
    }

    @Override
    public Result listAllTagsDetail() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagVo> tagVoList = copyList(tagList);
        return Result.success(tagVoList);
    }

    @Override
    public Result getTagsDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo tagVo = copy(tag);
        return Result.success(tagVo);
    }

    /**
     * Tag -> TagVo
     */
    public TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }

    /**
     * List<Tag> -> List<TagVo>
     */
    public List<TagVo> copyList(List<Tag> tagList){
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList){
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
}
