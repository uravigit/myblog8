package com.myblog8.service.impl;

import com.myblog8.entity.Post;
import com.myblog8.exception.ResourceNotFound;
import com.myblog8.payload.PostDto;
import com.myblog8.payload.PostResponse;
import com.myblog8.repository.PostRepository;
import com.myblog8.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public  class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());

        return dto;
    }

    @Override
    public void deletePostById(int postId) {
        Post post = postRepository.findById((long) postId).orElseThrow(

                ()->new ResourceNotFound("Post not found with id: "+postId)
        );

        postRepository.deleteById((long)postId);

    }

    @Override
    public PostDto getPostByPostId(int postId) {
        Post post = postRepository.findById((long) postId).orElseThrow(

                ()->new ResourceNotFound("Post not found with id: "+postId)
        );
        PostDto postDto = mapToDto(post);
        return postDto;
        //or(for both two lines of code) return mapToDto(post);

    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
       Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize,sort);
        Page<Post> all = postRepository.findAll(pageable);
        List<Post> posts = all.getContent();

        // List<Post> posts = postRepository.findAll();
        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();

        postResponse.setContent(dtos);
        postResponse.setPageNo(all.getNumber());
        postResponse.setTotalPages(all.getTotalPages());
        postResponse.setTotalElements((int)all.getTotalElements());
        postResponse.setPageSize(all.getSize());
        postResponse.setLast(all.isLast());


        return postResponse;
    }

    PostDto mapToDto(Post post){
        PostDto dto = modelMapper.map(post, PostDto.class);

        //  PostDto dto = new PostDto();
      //  dto.setId(post.getId());
      //  dto.setTitle(post.getTitle());
      //  dto.setDescription(post.getDescription());
      //  dto.setContent(post.getContent());
        return dto;
    }
}
